package ru.rusguardian.bot.command.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.Status;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.service.data.exception.EntityNotFoundException;
import ru.rusguardian.util.TelegramUtils;

import static ru.rusguardian.domain.TelegramDataEnum.WELCOME;

@Component
@Slf4j
public class StartCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = WELCOME;

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.START;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        createChatIfNotExists(update);

        String formattedMessage = String.format(TELEGRAM_DATA.getTextMessage(), TelegramUtils.getFirstname(update));
        SendMessage sendMessage = getDefaultSendMessageWithReplyKeyboard(update, formattedMessage);

        livingWageBot.execute(sendMessage);
    }

    private void createChatIfNotExists(Update update) {

        Long chatId = TelegramUtils.getChatId(update);
        try {
            chatService.findById(chatId);
        } catch (EntityNotFoundException e) {
            createDefaultUser(update);
        } finally {
            changeUserStatus(update, Status.NEW);
        }
    }


}
