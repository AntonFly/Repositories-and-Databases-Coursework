import Enums.UserState;
import model.Direction;
import model.Hotel;
import model.Room;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.telegram.telegrambots.bots.DefaultAbsSender;
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
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) throws TelegramApiException {
        Bot bot = new Bot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }

    TelegramUser user = new TelegramUser();

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
            switch (message.getText()) {
                case "/Start":
                    List<Direction> directions = session.createQuery("from Direction", Direction.class).list();
                    Set<String> countries = directions.stream().map(Direction::getCountry).collect(Collectors.toSet());
                    countries.forEach(country -> {
                        sendMsgWithBtn(message,
                                "Страна: " + country,
                                "Country " + country
                        );
                    });
                    break;
                default:
                    sendMsg(message, "Неизвестная команда");
            }
        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            Message message = update.getCallbackQuery().getMessage();
            switch (callBackData.split(" ")[0]) {
                case "Country":
                    List<Direction> directions = session.createQuery("from Direction D where D.country = :country", Direction.class)
                            .setParameter("country", "Россия")
                            .list();
                    Set<String> cities = directions.stream().map(Direction::getCity).collect(Collectors.toSet());
                    cities.forEach(city -> {
                        sendMsgWithBtn(message,
                                "Город: " + city,
                                "City " + city
                        );
                    });
                    break;
                case "/hotels":
                    List<Hotel> hotels = session.createQuery("from Hotel", Hotel.class).list();
                    user.state = UserState.HOTEL;
                    hotels.forEach(hotel -> {
                        sendMsgWithBtn(message,
                                "Отель: " + hotel.getName() +
                                        "\r\nТип отеля: " + hotel.getHotelType().getName() +
                                        "\r\nУровень отеля: " + hotel.getHotelClass().getName() +
                                        "\r\nРасположение отеля: " + hotel.getDisposition() +
                                        "\r\nМестоположение: " + hotel.getDirection().getCountry() + " " + hotel.getDirection().getCity() +
                                        "\r\nПогодные условия: " + hotel.getDirection().getClimate(),
                                "Hotel " + hotel.getId()
                        );
                    });

                case "/rooms":
                    //List<Room> =session.createQuery("from Room where Hotel.id ")
                    break;
                case "/facilities":
                    break;
                case "/excursions":
                    break;
                case "/additional_servises":
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


    public synchronized void sendMsgWithBtn(Message message, String text, String data) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Выбрать");
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
