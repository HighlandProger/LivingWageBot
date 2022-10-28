package ru.rusguardian.bot.command.main.start.admin;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rusguardian.util.TelegramUtils;

public interface AdminService {


    default SendPhoto getSendPhotoFromUpdate(Update update) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(TelegramUtils.getChatId(update));
        sendPhoto.setPhoto(TelegramUtils.getPhotoInputFile(update));
        sendPhoto.setCaption(TelegramUtils.getCaption(update));

        return sendPhoto;
    }

    default SendVideo getSendVideoFromUpdate(Update update) {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(TelegramUtils.getChatId(update));
        sendVideo.setVideo(TelegramUtils.getVideoInputFile(update));
        sendVideo.setCaption(TelegramUtils.getCaption(update));

        return sendVideo;
    }

    default boolean isUpdateHasPhoto(Update update) {
        return update.getMessage().hasPhoto();
    }

    default boolean isUpdateHasVideo(Update update) {
        return update.getMessage().hasVideo();
    }
}
