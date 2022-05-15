import Enums.UserState;
import model.Hotel;
import org.hibernate.Session;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    static Session session = HibernateUtil.getSessionFactory().openSession();
    public static List<?> HandleMessage(Message message, TelegramUser user ){
        switch (message.getText()) {
            case "/hotels":
                List<Hotel> hotels = session.createQuery("from Hotel", Hotel.class).list();
                user.state = UserState.HOTEL;
                return hotels;
            case "/rooms":
                break;
            case "/facilities":
                break;
            case "/excursions":
                break;
            case "/additional_servises":
                break;
            default:
                return new ArrayList<>();
        }
        return new ArrayList<>();
    }
}

