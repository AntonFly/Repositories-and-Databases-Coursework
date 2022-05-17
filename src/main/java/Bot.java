import Enums.UserState;
import com.sun.org.apache.xpath.internal.operations.Or;
import model.*;
import org.hibernate.Session;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import persistence.HibernateUtil;

import java.io.File;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) throws TelegramApiException {
        Bot bot = new Bot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }

    UserState state=UserState.IDLE;
    Room room;
    User user;
    Order order;

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public String getBotToken() {
        return "5362808314:AAGDGopIRxbci0SYmVFPD7jI64yl4pY_EC8";
    }

    @Override
    public String getBotUsername() {
        return "@FlyTourBookBot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        if (update.hasMessage()) {
            Message message = update.getMessage();
            switch (state){
                case IDLE:
                    switch (message.getText()) {
                        case "/directions":
                            List<String> countries = session.createQuery("select distinct country from Direction D", String.class).list();
                            countries.forEach(country -> {
                                sendMsgWithOneBtn(message,
                                      "Страна: " + country,
                                       "Country " + country,
                                        "Выбрать"
                                );
                             });
                            break;
                        case "/orders":
                            List<User> u = session.createQuery("from User U where U.chat_id = :chat_id",User.class)
                                    .setParameter("chat_id", message.getChatId())
                                            .list();
                            if(u.size()==0)
                                sendMsg(message,"Ваш список заказов пуст");
                            else{
                                final String[] msg = {"Заказы:\r\n"};
                                final int[] i = {1};
                                u.get(0).getOrders().forEach(ord->{
                                    msg[0]+=i[0]+". Отель: "+ord.getHotel().getName()+" Номер: "+ ord.getRoom().getRoomCategory().getName()+
                                            " Дата заезда: " + ord.getDateStart()+" Дата выезда: " + ord.getDateEnd()+"\r\n";
                                });
                                sendMsg(message,msg[0]);
                            }
                            break;
                        default:
                            sendMsg(message, "Неизвестная команда");
                    }
                    break;
                case NAME:
                    switch (message.getText()){
                        case "/break":
                            state = UserState.IDLE;
                            break;
                        default:
                            user.setName(message.getText());
                            state =UserState.SURNAME;
                            sendMsg(message, "Введите вашу фамилию (для прекращения регистрации введите /break):");
                            break;
                    }
                    break;
                case SURNAME:
                    switch (message.getText()){
                        case "/break":
                            state = UserState.IDLE;
                            break;
                        default:
                            user.setSurname(message.getText());
                            state =UserState.PHONE;
                            sendMsg(message, "Введите ваш телефон (для прекращения регистрации введите /break):");
                            break;
                    }
                    break;
                case PHONE:
                    switch (message.getText()){
                        case "/break":
                            state = UserState.IDLE;
                            break;
                        default:
                            long phone;
                            try{
                                phone = Long.parseLong(message.getText());
                            }catch (Exception e){
                                phone = 87776665544l;
                            }
                            user.setPhoneNumber(phone);
                            state =UserState.EMAIL;
                            sendMsg(message, "Введите ваш Email (для прекращения регистрации введите /break):");
                            break;
                    }
                    break;
                case EMAIL:
                    switch (message.getText()){
                        case "/break":
                            state = UserState.IDLE;
                            break;
                        default:
                            user.setEmail(message.getText());
                            state =UserState.PROCESSING;
                            sendMsg(message, "Подождите, выполняется регистрация.");
                            session.beginTransaction();
                            session.save(user);
                            session.getTransaction().commit();
                            List<User> newUser= session.createQuery("from User U where U.chat_id = :chat_id", User.class)
                                .setParameter("chat_id",message.getChatId())
                                .list();
                            if(newUser.size()==0){
                                state = UserState.IDLE;
                                sendMsg(message, "Не получилось зарегестрировать пользователся, повторите попытку");
                            }
                            else{
                                user = newUser.get(0);
                                state = UserState.FIRSTDAME;
                                sendMsg(message, "Пользовотель успешно зарегестрирован.\r\n" +
                                        "Введите дату начала брони в формате dd.mm.yyyy (для прекращения бронирования введите /break):");
                                order = new Order();
                                order.setUser(user);
                                order.setRoom(this.room);
                                order.setHotel(this.room.getHotel());

                            }
                            break;
                    }
                    break;
                case FIRSTDAME:
                    switch (message.getText()){
                        case "/break":
                            state = UserState.IDLE;
                            break;
                        default:
                            try{
                            java.sql.Date date = new java.sql.Date(
                                ((java.util.Date) new SimpleDateFormat("dd.MM.yyyy").parse(message.getText())).getTime());
                                order.setDateStart(date);
                                state = UserState.SECONDDATE;
                                sendMsg(message, "Введите дату конца брони в формате dd.MM.yyyy (для прекращения бронирования введите /break):");
                            }catch (ParseException e){
                                sendMsg(message, "Вы ввели дату в неверном формате, необходимый формат dd.MM.yyyy (для прекращения бронирования введите /break):");
                            }

                            break;
                    }
                    break;
                case SECONDDATE:
                    switch (message.getText()){
                        case "/break":
                            state = UserState.IDLE;
                            break;
                        default:
                            try{
                                java.sql.Date date = new java.sql.Date(
                                        ((java.util.Date) new SimpleDateFormat("dd.MM.yyyy").parse(message.getText())).getTime());
                                order.setDateEnd(date);
                                state = UserState.PENDINGCONFIRMATION;

                                setUpMsgWithTwoButton(message,
                                        "Вы выбрали:\r\n" +
                                                "Номер: "+order.getRoom().getRoomCategory().getName()+
                                        "\r\nВ отеле: "+ order.getHotel().getName()+
                                                "\r\n C "+ sdf.format(order.getDateStart())+" по "+ sdf.format(order.getDateEnd())+
                                                "\r\n Ваш Email: "+ order.getUser().getEmail(),

                                        "Подтвердить",
                                        "Отменить",
                                        "Confirmation ",
                                        "Rejection "

                                );
                            }catch (ParseException e){
                                sendMsg(message, "Вы ввели дату в неверном формате, необходимый формат dd.MM.yyyy (для прекращения бронирования введите /break):");
                            }

                            break;
                    }
                    break;
                case PENDINGCONFIRMATION:
                    switch (message.getText()){
                        case "/break":
                            state = UserState.IDLE;
                            break;
                        default:
                            sendMsg(message,"Неизвестная команда.");
                    }
                    break;






            }
        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            Message message = update.getCallbackQuery().getMessage();
            Hotel hotel;
            Room room;
            switch (callBackData.split(" ")[0]) {
                case "Country":
                    List<Direction> directions = session.createQuery("from Direction D where D.country = :country", Direction.class)
                            .setParameter("country", callBackData.split(" ")[1])
                            .list();
                    Set<String> cities = directions.stream().map(Direction::getCity).collect(Collectors.toSet());
                    cities.forEach(city -> {
                        sendMsgWithOneBtn(message,
                                "Город: " + city,
                                "City " + city,
                                "Выбрать"
                        );
                    });
                    break;
                case "City":

                    List<Hotel> hotels = session.createQuery("from Hotel H where H.direction.city = :city", Hotel.class)
                            .setParameter("city",callBackData.split(" ")[1])
                            .list();
                    hotels.forEach(h -> {
                        sendHotelMsg(message,
                                "Отель: " + h.getName() +
                                        "\r\nТип отеля: " + h.getHotelType().getName() +
                                        "\r\nУровень отеля: " + h.getHotelClass().getName() +
                                        "\r\nРасположение отеля: " + h.getDisposition() +
                                        "\r\nМестоположение: " + h.getDirection().getCountry() + " " + h.getDirection().getCity() +
                                        "\r\nПогодные условия: " + h.getDirection().getClimate(),
                                "Hotel " + h.getId(),
                                "Excursions "+ h.getId()
                        );
                    });
                    break;

                case "Hotel":
                    hotel = session.get(Hotel.class,Integer.parseInt(callBackData.split(" ")[1]));
                    hotel.getRooms().forEach(r ->{
                        String msg = "Тип комнаты: "+ r.getRoomCategory().getName()+
                                "\r\nВместимость: "+ r.getRoomCategory().getCapacity()+
                                "\r\nСтоимость: " + r.getRoomCategory().getPrice() + r.getRoomCategory().getCurrency();
                        if(r.getRoomCategory().getExtraPlace()>0)
                            msg += "\r\nДополнительные места: "+ r.getRoomCategory().getExtraPlace()+" по "+
                                    r.getRoomCategory().getPriceOfExtraPlace()+ r.getRoomCategory().getCurrency();

                        sendRoomMsg(message,msg,"Room "+r.getId(),"Facilities " + r.getId());
                    });
                    break;
                case "Excursions":
                    hotel = session.get(Hotel.class,Integer.parseInt(callBackData.split(" ")[1]));
                    if(hotel.getExcursions().size()==0)
                        sendMsg(message,"Отель: "+hotel.getName()+ " не предоставляет экскурсий.");
                    hotel.getExcursions().forEach(exc -> {
                        String msg ="Экскурсия: "+ exc.getName()+
                                "\r\nЛокация: " + exc.getLocation()+
                                "\r\nСтоимость за место: "+ exc.getPricePerPerson()+" "+exc.getCurrency()+
                                "\r\nМаксимальное число участников: " + exc.getMaxMembers()+
                                "\r\nМинимальный возраст: " + exc.getMinAge();
                        if(exc.getGuide() == 1)
                            msg +="\r\nГид на языке: " + exc.getLanguage();
                        else msg += "\r\n<Без гида";
                        sendMsg(message,msg);
                    });
                    break;
                case "Facilities":
                    room = session.get(Room.class, Integer.parseInt(callBackData.split(" ")[1]));
                    final String[] msg = {"Удобства: \r\n"};
                    final int[] i = {1};
                    room.getRoomCategory().getFacilities().forEach(fac->{
                        msg[0] += i[0] +". "+ fac.getName()+"\r\n";
                        i[0]++;
                    });
                    sendMsg(message, msg[0]);


                    break;
                case "Room":
                    switch (callBackData.split(" ")[1]){
                        case "Register":
                            state = UserState.NAME;
                            user = new User();
                            user.setChatId(message.getChatId());
                            this.room = session.get(Room.class, Integer.parseInt(callBackData.split(" ")[2]));
                            sendMsg(message, "Введите ваше имя (для прекращения регистрации введите /break):");
                            break;
                        default:
                            List<User> us = session.createQuery("from User U where U.chat_id = :chat_id", User.class)
                                    .setParameter("chat_id",message.getChatId())
                                    .list();
                            if(us.size()==0){
                                String roomId = callBackData.split(" ")[1];
                                sendRegisterMsg(message,
                                        "Вы не зарегестрированы.\r\n" +
                                                "Хотите создать пользователя и забронировать номер?",
                                        "Room Register "+roomId
                                );}
                            else{
                                user = us.get(0);
                                state = UserState.FIRSTDAME;
                                this.room = session.get(Room.class, Integer.parseInt(callBackData.split(" ")[1]));
                                sendMsg(message, "Введите дату начала брони в формате dd.mm.yyyy (для прекращения бронирования введите /break):");
                                order = new Order();
                                order.setUser(user);
                                order.setRoom(this.room);
                                order.setHotel(this.room.getHotel());
                            }

                            break;
                    }
                    break;
                case "Confirmation":
                    if(state != UserState.PENDINGCONFIRMATION)
                        break;
                    session.beginTransaction();
                    int orderId = (int) session.save(order);
                    System.out.println( orderId);
                    session.getTransaction().commit();
                    if(!session.get(Order.class, orderId).equals(null)){
                        sendMsg(message, "Номер успешно забронирован");
                        state = UserState.IDLE;
                    }else {
                        sendMsg(message, "Что-то пошло не так, попробуйте снова");
                        state = UserState.IDLE;
                    }
                    break;
                case "Rejection":
                    state =UserState.IDLE;
                    sendMsg(message, "Бронирование отменено");
                    break;
                default:
                    sendMsg(message, "Неизвестная команда");
            }
        }
    }


    public synchronized void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public synchronized void sendMsgWithOneBtn(Message message, String text, String data,String btnText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(btnText);
        inlineKeyboardButton.setCallbackData(data);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        firstRow.add(inlineKeyboardButton);
        rowList.add(firstRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendHotelMsg(Message message, String text, String data, String excursions) {
        setUpMsgWithTwoButton(message,text,"Выбрать","Экскурсии",data,excursions);
    }

    public synchronized void sendRoomMsg(Message message, String text, String data, String facilities) {
        setUpMsgWithTwoButton(message,text,"Забронировать","Удобства",data,facilities);
    }

    public synchronized void sendRegisterMsg(Message message, String text, String data){
        sendMsgWithOneBtn(message,text,data,"Создать пользователя");
    }

    private void setUpMsgWithTwoButton(Message message, String msgText,String firstBtnText, String optionalBtnText, String data1, String  data2) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(msgText);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(firstBtnText);
        inlineKeyboardButton1.setCallbackData(data1);
        inlineKeyboardButton2.setText(optionalBtnText);
        inlineKeyboardButton2.setCallbackData(data2);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        firstRow.add(inlineKeyboardButton1);
        firstRow.add(inlineKeyboardButton2);
        rowList.add(firstRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendImage(Message message, String file, String discription) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(String.valueOf(message.getChatId()));
        sendPhotoRequest.setPhoto(new InputFile(new File(file)));
        sendPhotoRequest.setCaption(discription);
        try {
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
