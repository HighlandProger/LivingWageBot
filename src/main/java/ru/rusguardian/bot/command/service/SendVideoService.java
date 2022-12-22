package ru.rusguardian.bot.command.service;

import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

public interface SendVideoService {

    Command getCommand();

    default SendVideo getSendVideoByTelegramData(Update update, TelegramDataEnum telegramDataEnum) {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(TelegramUtils.getChatId(update));
        sendVideo.setVideo(new InputFile(telegramDataEnum.getVideoId()));
        sendVideo.setCaption(telegramDataEnum.getTextMessage());

        return sendVideo;
    }

    default SendVideo getDefaultSendVideoWithReplyKeyboard(Update update, TelegramDataEnum telegramDataEnum) {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(TelegramUtils.getChatId(update));
        sendVideo.setVideo(new InputFile(telegramDataEnum.getVideoId()));
        sendVideo.setCaption(telegramDataEnum.getTextMessage());
        sendVideo.setReplyMarkup(getCommand().getDefaultReplyKeyboard(update));

        return sendVideo;
    }
}
