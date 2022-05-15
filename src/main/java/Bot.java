import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    HashMap<Long,TelegramUser> users = new HashMap<Long, TelegramUser >();

    @Override
    public String getBotToken(){
        return "5362808314:AAGDGopIRxbci0SYmVFPD7jI64yl4pY_EC8";
    }
    @Override
    public String getBotUsername(){
        return "@FlyTourBookBot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Message message = update.getMessage();
            if(users.get(message.getChatId()).equals(null)) {
               users.put(message.getChatId(), new TelegramUser(message.getChatId()));
               //TODO make login
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

    public void sendImage(Message message, String file,String discription ) {
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
