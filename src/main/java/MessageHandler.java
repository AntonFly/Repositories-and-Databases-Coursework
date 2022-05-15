import org.telegram.telegrambots.meta.api.objects.Message;

public class MessageHandler {
    public static String HandleMessage(Message message, TelegramUser user){
        switch (message.getText()) {
            case "/hotels":
                break;
            case "/rooms":
                break;
            case "/facilities":
                break;
            case "/excursions":
                break;
            case "/additional_servises":
                break;

        }
        return "";
    }
}
