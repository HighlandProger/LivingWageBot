package ru.rusguardian.bot.command.main.start.admin.action;

import lombok.extern.slf4j.Slf4j;
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
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.CONFIRM_ACTION;

@Component
@Slf4j
public class _3_ConfirmActionCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = CONFIRM_ACTION;

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
            sendPhoto.setCaptionEntities(update.getCallbackQuery().getMessage().getCaptionEntities());

            sendToAllUsers(sendPhoto);
        } else if (isUpdateHasVideo(update)) {
            SendVideo sendVideo = new SendVideo();
            sendVideo.setVideo(new InputFile(update.getCallbackQuery().getMessage().getVideo().getFileId()));
            sendVideo.setCaption(update.getCallbackQuery().getMessage().getCaption());
            sendVideo.setCaptionEntities(update.getCallbackQuery().getMessage().getCaptionEntities());

            sendToAllUsers(sendVideo);
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(update.getCallbackQuery().getMessage().getText());
            sendMessage.setEntities(TelegramUtils.getMessageEntities(update));

            sendToAllUsers(sendMessage);
        }

        SendMessage sendMessage = getSendMessageWithTelegramDataAndReplyKeyboard(update, TELEGRAM_DATA, replyButtonLines);
        livingWageBot.execute(sendMessage);
        chatService.updateChatStatus(TelegramUtils.getChatId(update), Status.EXECUTED);
    }

    private void sendToAllUsers(SendPhoto message) throws TelegramApiException {
        for (Chat chat : chatService.getAll()) {
            message.setChatId(chat.getId());
            try {
                livingWageBot.execute(message);
                log.debug("Sent action to user with id = {}", chat.getId());
            } catch (TelegramApiException e){
                log.error("Could not send action to user with id = {}", chat.getId());
                log.error(e.getMessage());
            }
        }
    }

    private void sendToAllUsers(SendVideo message) throws TelegramApiException {
        for (Chat chat : chatService.getAll()) {
            message.setChatId(chat.getId());
            try {
                livingWageBot.execute(message);
                log.debug("Sent action to user with id = {}", chat.getId());
            } catch (TelegramApiException e){
                log.error("Could not send action to user with id = {}", chat.getId());
                log.error(e.getMessage());
            }
        }
    }

    private void sendToAllUsers(SendMessage message) throws TelegramApiException {
        for (Chat chat : chatService.getAll()) {
            message.setChatId(chat.getId());
            try {
                livingWageBot.execute(message);
                log.debug("Sent action to user with id = {}", chat.getId());
            } catch (TelegramApiException e){
                log.error("Could not send action to user with id = {}", chat.getId());
                log.error(e.getMessage());
            }
        }
    }

    private boolean isUpdateHasPhoto(Update update) {
        return update.getCallbackQuery().getMessage().hasPhoto();
    }

    private boolean isUpdateHasVideo(Update update) {
        return update.getCallbackQuery().getMessage().hasVideo();
    }
}
