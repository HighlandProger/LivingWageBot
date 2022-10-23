package ru.rusguardian.bot.command.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.Status;
import ru.rusguardian.service.data.exception.EntityNotFoundException;
import ru.rusguardian.util.TelegramUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class StartCommand extends Command {

    private static final String TELEGRAM_DATA_NAME = "WELCOME";
    private static final Map<String, String> inlineCommandButtonsMap = new HashMap<>();

    static {
        inlineCommandButtonsMap.put("Выбрать регион", "Выбрать регион");
    }

    @Override
    protected CommandName getType() {
        return CommandName.START;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        createChatIfNotExists(update);

        SendMessage sendMessage = getSendMessage(update);

        livingWageBot.execute(sendMessage);
    }

    private void createChatIfNotExists(Update update) {

        Long chatId = TelegramUtils.getChatId(update);
        String username = TelegramUtils.getUsername(update);
        try {
            chatService.findById(chatId);
            changeUserStatus(update, Status.NEW);
        } catch (EntityNotFoundException e) {
            log.debug("Chat with id = {} not found", chatId);
            Chat defaultChat = new Chat(chatId, username, Status.NEW, 1, 0, 0, null, new ArrayList<>());
            chatService.create(defaultChat);
        }
    }

    private SendMessage getSendMessage(Update update){

        String callbackMessage = getCallbackMessage(update);
        return getSendMessageWithInlineKeyboard(update, callbackMessage, inlineCommandButtonsMap);
    }

    private String getCallbackMessage(Update update){
        String userFirstName = update.getMessage().getFrom().getFirstName();
        String patternMessage = telegramDataService.getTelegramDataByName(TELEGRAM_DATA_NAME).getTextMessage();
        return String.format(patternMessage, userFirstName);
    }

}
