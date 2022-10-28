package ru.rusguardian.bot.command.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

import java.util.List;

public interface SendMessageService {

    Command getCommand();

    default SendMessage getSendMessageByTelegramData(Update update, TelegramDataEnum telegramDataEnum) {
        String message = telegramDataEnum.getTextMessage();
        return getSimpleSendMessage(update, message);
    }

    default SendMessage getSimpleSendMessage(Update update, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(TelegramUtils.getChatId(update));
        sendMessage.setText(message);

        return sendMessage;
    }

    default SendMessage getDefaultSendMessageWithReplyKeyboard(Update update, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(TelegramUtils.getChatId(update));
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(getCommand().getDefaultReplyKeyboard(update));

        return sendMessage;
    }

    default SendMessage getSendMessageWithTelegramDataAndReplyKeyboard(Update update, TelegramDataEnum telegramDataEnum, List<List<String>> replyButtonLines) {
        SendMessage sendMessage = getSendMessageByTelegramData(update, telegramDataEnum);
        sendMessage.setReplyMarkup(getCommand().getReplyKeyboard(replyButtonLines));

        return sendMessage;
    }

    default SendMessage getSendMessageWithTelegramDataAndInlineKeyboard(Update update, TelegramDataEnum telegramDataEnum, List<String> inlineButtonLines) {
        SendMessage sendMessage = getSendMessageByTelegramData(update, telegramDataEnum);
        sendMessage.setReplyMarkup(getCommand().getMultipleLinedInlineKeyboard(inlineButtonLines));

        return sendMessage;
    }
}
