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
import ru.rusguardian.domain.TelegramData;
import ru.rusguardian.service.data.exception.EntityNotFoundException;
import ru.rusguardian.util.TelegramUtils;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class StartCommand extends Command {

    private static final String TELEGRAM_DATA_NAME = "WELCOME";
    private static final String CHOOSE_REGION_BUTTON_NAME = "Выбрать регион";
    private static final String CHOOSE_REGION_BUTTON_CALLBACK_DATA = "Выбрать регион";
    private static final Map<String, String> inlineCommandButtonsMap = new HashMap<>();

    static {
        inlineCommandButtonsMap.put(CHOOSE_REGION_BUTTON_NAME, CHOOSE_REGION_BUTTON_CALLBACK_DATA);
    }

    @Override
    protected CommandName getType() {
        return CommandName.START;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        createChatIfNotExists(update);

        TelegramData welcomeData = appDataService.getTelegramDataByName(TELEGRAM_DATA_NAME).orElseThrow();
        String returnMessage = String.format(welcomeData.getTextMessage(), update.getMessage().getFrom().getFirstName());

        SendMessage sendMessage = getSendMessageWithInlineKeyboard(update, returnMessage, inlineCommandButtonsMap);

        livingWageBot.execute(sendMessage);
    }

    private void createChatIfNotExists(Update update) {

        Long chatId = TelegramUtils.getChatId(update);
        String username = TelegramUtils.getUsername(update);
        try {
            chatService.findById(chatId);
            chatService.updateChatStatus(chatId, Status.NEW);
        } catch (EntityNotFoundException e) {
            log.debug("Chat with id = {} not found", chatId);
            chatService.create(new Chat(chatId, username, Status.NEW, 1, 0, 0, null, null));
        }
    }

}
