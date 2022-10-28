package ru.rusguardian.bot.command.main.start.admin.action;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.Status;
import ru.rusguardian.domain.TelegramDataDto;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.CONFIRM_ACTION;
import static ru.rusguardian.domain.TelegramDataEnum.STOCKS;

@Component
public class _3_ConfirmActionCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = CONFIRM_ACTION;
    private static final TelegramDataEnum UPDATE_TELEGRAM_DATA = STOCKS;

    private static final List<List<String>> replyButtonLines = new ArrayList<>();
    private static final List<String> firstLineButtons = new ArrayList<>();

    private static final String MAIN_MENU_BUTTON = "\uD83C\uDFE0В главное меню";

    static {
        firstLineButtons.add(MAIN_MENU_BUTTON);

        replyButtonLines.add(firstLineButtons);
    }

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.CONFIRM_ACTION;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        if (isUpdateHasPhoto(update)) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setPhoto(new InputFile(update.getCallbackQuery().getMessage().getPhoto().get(0).getFileId()));
            sendPhoto.setCaption(update.getCallbackQuery().getMessage().getCaption());

            updateStocks(sendPhoto);
            sendToAllUsers(sendPhoto);
        } else if (isUpdateHasVideo(update)) {
            SendVideo sendVideo = new SendVideo();
            sendVideo.setVideo(new InputFile(update.getCallbackQuery().getMessage().getVideo().getFileId()));
            sendVideo.setCaption(update.getCallbackQuery().getMessage().getCaption());

            updateStocks(sendVideo);
            sendToAllUsers(sendVideo);
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(update.getCallbackQuery().getMessage().getText());

            updateStocks(sendMessage);
            sendToAllUsers(sendMessage);
        }

        SendMessage sendMessage = getSendMessageWithTelegramDataAndReplyKeyboard(update, TELEGRAM_DATA, replyButtonLines);
        livingWageBot.execute(sendMessage);
        chatService.updateChatStatus(TelegramUtils.getChatId(update), Status.EXECUTED);
    }

    private void sendToAllUsers(SendPhoto message) throws TelegramApiException {
        for (Chat chat : chatService.getAll()) {
            message.setChatId(chat.getId());
            livingWageBot.execute(message);
        }
    }

    private void sendToAllUsers(SendVideo message) throws TelegramApiException {
        for (Chat chat : chatService.getAll()) {
            message.setChatId(chat.getId());
            livingWageBot.execute(message);
        }
    }

    private void sendToAllUsers(SendMessage message) throws TelegramApiException {
        for (Chat chat : chatService.getAll()) {
            message.setChatId(chat.getId());
            livingWageBot.execute(message);
        }
    }

    private void updateStocks(SendPhoto message) {
        TelegramDataDto telegramDataDto = new TelegramDataDto();
        telegramDataDto.setName(UPDATE_TELEGRAM_DATA.name());
        telegramDataDto.setPhotoId(message.getPhoto().getAttachName());
        telegramDataDto.setTextMessage(message.getCaption());

        telegramDataService.updateTelegramData(telegramDataDto);
    }

    private void updateStocks(SendVideo message) {
        TelegramDataDto telegramDataDto = new TelegramDataDto();
        telegramDataDto.setName(UPDATE_TELEGRAM_DATA.name());
        telegramDataDto.setVideoId(message.getVideo().getAttachName());
        telegramDataDto.setTextMessage(message.getCaption());

        telegramDataService.updateTelegramData(telegramDataDto);
    }

    private void updateStocks(SendMessage message) {
        TelegramDataDto telegramDataDto = new TelegramDataDto();
        telegramDataDto.setName(UPDATE_TELEGRAM_DATA.name());
        telegramDataDto.setTextMessage(message.getText());

        telegramDataService.updateTelegramData(telegramDataDto);
    }

    private boolean isUpdateHasPhoto(Update update) {
        return update.getCallbackQuery().getMessage().hasPhoto();
    }

    private boolean isUpdateHasVideo(Update update) {
        return update.getCallbackQuery().getMessage().hasVideo();
    }
}
