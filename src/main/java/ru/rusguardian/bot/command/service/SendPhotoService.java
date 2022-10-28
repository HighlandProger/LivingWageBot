package ru.rusguardian.bot.command.service;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

public interface SendPhotoService {

    Command getCommand();

    default SendPhoto getSendPhotoByTelegramData(Update update, TelegramDataEnum telegramDataEnum) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(TelegramUtils.getChatId(update));
        sendPhoto.setPhoto(new InputFile(telegramDataEnum.getPhotoId()));
        sendPhoto.setCaption(telegramDataEnum.getTextMessage());

        return sendPhoto;
    }

    default SendPhoto getDefaultSendPhotoWithReplyKeyboard(Update update, TelegramDataEnum telegramDataEnum) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(TelegramUtils.getChatId(update));
        sendPhoto.setPhoto(new InputFile(telegramDataEnum.getPhotoId()));
        sendPhoto.setCaption(telegramDataEnum.getTextMessage());
        sendPhoto.setReplyMarkup(getCommand().getDefaultReplyKeyboard(update));

        return sendPhoto;
    }
}
