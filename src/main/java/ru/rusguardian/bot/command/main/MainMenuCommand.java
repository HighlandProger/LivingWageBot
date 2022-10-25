package ru.rusguardian.bot.command.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.TelegramDataEnum;

import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.NOT_FOUND;

@Component
@Slf4j
public class MainMenuCommand extends Command {

    private static final TelegramDataEnum TELEGRAM_DATA = NOT_FOUND;

    @Override
    protected CommandName getType() {
        return CommandName.MAIN_MENU;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        List<List<String>> replyButtonLines = getMainReplyButtonLines();
        SendMessage sendMessage = getSendMessageWithTelegramDataAndReplyKeyboard(update, TELEGRAM_DATA, replyButtonLines);

        livingWageBot.execute(sendMessage);
    }


}
