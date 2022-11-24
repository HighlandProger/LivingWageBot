package ru.rusguardian.util;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.NoSuchElementException;

public class TelegramUtils {

    private TelegramUtils() {
    }

    public static String getChatIdString(Update update) {

        String chatIdStr = "";
        if (update.hasMessage()) {
            chatIdStr = update.getMessage().getChatId().toString();
        }
        if (update.hasCallbackQuery()) {
            chatIdStr = update.getCallbackQuery().getFrom().getId().toString();
        }
        return chatIdStr;
    }

    public static int getMessageId(Update update) {
        return update.getCallbackQuery().getMessage().getMessageId();
    }

    public static Long getChatId(Update update) {
        Long chatId = null;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getFrom().getId();
        }
        return chatId;
    }

    public static InputFile getPhotoInputFile(Update update) {
        String photoId = "";
        if (update.hasMessage()) {
            photoId = update.getMessage().getPhoto().get(0).getFileId();
        }
        if (update.hasCallbackQuery()) {
            photoId = update.getCallbackQuery().getMessage().getPhoto().get(0).getFileId();
        }
        return new InputFile(photoId);
    }

    public static InputFile getVideoInputFile(Update update) {
        String videoId = "";
        if (update.hasMessage()) {
            videoId = update.getMessage().getVideo().getFileId();
        }
        if (update.hasCallbackQuery()) {
            videoId = update.getCallbackQuery().getMessage().getVideo().getFileId();
        }
        return new InputFile(videoId);
    }

    public static String getCaption(Update update) {
        String caption = "";
        if (update.hasMessage()) {
            caption = update.getMessage().getCaption();
        }
        if (update.hasCallbackQuery()) {
            caption = update.getCallbackQuery().getMessage().getCaption();
        }
        return caption;
    }

    public static String getUsername(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getUserName();
        } else {
            return update.getCallbackQuery().getFrom().getUserName();
        }
    }

    public static String getFirstname(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getFirstName();
        } else {
            return update.getCallbackQuery().getFrom().getFirstName();
        }
    }

    public static String getCallbackQueryId(Update update) {
        return update.getCallbackQuery().getId();
    }

    public static String getTextFromUpdate(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getText();
        } else return update.getCallbackQuery().getData();
    }

    public static List<MessageEntity> getMessageEntities(Update update){
        if (update.hasMessage()){
            return update.getMessage().getCaptionEntities();
        }
        if (update.hasCallbackQuery()){
            return update.getCallbackQuery().getMessage().getCaptionEntities();
        }
        throw new NoSuchElementException("Cannot get message entities");
    }
}
