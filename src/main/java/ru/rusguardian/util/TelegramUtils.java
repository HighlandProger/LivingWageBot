package ru.rusguardian.util;

import org.telegram.telegrambots.meta.api.objects.Update;

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

    public static String getUsername(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getUserName();
        } else {
            return update.getCallbackQuery().getFrom().getUserName();
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
}
