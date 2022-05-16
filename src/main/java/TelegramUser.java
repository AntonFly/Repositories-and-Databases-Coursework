import Enums.UserState;

public class TelegramUser {

    boolean loginflag = false;
    boolean logined = false;
    UserState state = null;
    Long chatId = null;
    String login;
    //    List<Requests> invites=new ArrayList();
    boolean isinvites = false;

    public TelegramUser(Long chatId) {
        this.chatId = chatId;
    }

    public TelegramUser() {
    }

    public boolean isLoginflag() {
        return loginflag;
    }

    public void setLoginflag(boolean loginflag) {
        this.loginflag = loginflag;
    }
}

