import Enums.UserState;
import model.Direction;
import model.Hotel;
import model.Room;
import model.User;
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
import java.util.ArrayList;
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
    String roomId;
    User user;

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
//            if(users.get(message.getChatId()).equals(null)) {
//               users.put(message.getChatId(), new TelegramUser(message.getChatId()));
//               //TODO make login
//            }
            switch (state){
                case IDLE:
                    switch (message.getText()) {
                        case "/Start":
                            List<Direction> directions = session.createQuery("from Direction", Direction.class).list();
                            Set<String> countries = directions.stream().map(Direction::getCountry).collect(Collectors.toSet());
                            countries.forEach(country -> {
                                sendMsgWithOneBtn(message,
                                      "Страна: " + country,
                                       "Country " + country,
                                        "Выбрать"
                                );
                             });
                            break;
                        default:
                            sendMsg(message, "Неизвестная команда");
                    }
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
                            if(session.createQuery("from User U where U.chat_id = :chat_id", User.class)
                                .setParameter("chat_id",message.getChatId())
                                .list().size()==0)
                                sendMsg(message, "Не получилось зарегестрировать пользователся");
                                    else
                                        sendMsg(message, "Пользовотель успешно зарегестрирован.\r\n" +
                                                "Введите дату начала брони в формате (dd.mm.yyyy");
                            break;
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
                        msg[0] += i[0] + fac.getName();
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
                            roomId = callBackData.split(" ")[2];
                            sendMsg(message, "Введите ваше имя (для прекращения регистрации введите /break):");
                            break;
                        default:
                            List<User> user = session.createQuery("from User U where U.chat_id = :chat_id", User.class)
                                    .setParameter("chat_id",message.getChatId())
                                    .list();
                            if(user.size()==0){
                                String roomId = callBackData.split(" ")[1];
                                sendRegisterMsg(message,
                                        "Вы не зарегестрированы.\r\n" +
                                                "Хотите создать пользователя и забронировать номер?",
                                        "Room Register "+roomId
                                );}
                            break;
                    }

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
        setUpMsgWithTwoButton(message,text,"Забронировать","Экскурсии",data,excursions);
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
