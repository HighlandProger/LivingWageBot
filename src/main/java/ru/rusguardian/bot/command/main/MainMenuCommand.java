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
import ru.rusguardian.util.TelegramUtils;

import static ru.rusguardian.domain.TelegramDataEnum.MAIN_MENU;

@Component
@Slf4j
public class MainMenuCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = MAIN_MENU;

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.MAIN_MENU;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendMessage sendMessage = getDefaultSendMessageWithReplyKeyboard(update, TELEGRAM_DATA.getTextMessage());

        livingWageBot.execute(sendMessage);
        chatService.updateChatStatus(TelegramUtils.getChatId(update), Status.EXECUTED);
    }
}
