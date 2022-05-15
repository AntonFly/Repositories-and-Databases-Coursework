public class TelegramUser {
    boolean loginflag = false;
    boolean logined = false;
    Long chatId = null;
    String login;
    //    List<Requests> invites=new ArrayList();
    boolean isinvites = false;

    public TelegramUser(Long chatId) {
        this.chatId = chatId;
    }

    public boolean isLoginflag() {
        return loginflag;
    }

    public void setLoginflag(boolean loginflag) {
        this.loginflag = loginflag;
    }
}

