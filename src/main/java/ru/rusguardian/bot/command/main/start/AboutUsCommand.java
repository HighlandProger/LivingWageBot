package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.TelegramDataEnum;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.ABOUT_US;

@Component
public class AboutUsCommand extends Command {

    private static final TelegramDataEnum TELEGRAM_DATA = ABOUT_US;

    private static final List<List<String>> replyButtonLines = new ArrayList<>();
    private static final List<String> firstLineButtons = new ArrayList<>();
    private static final String MAIN_MENU_BUTTON = "В главное меню";

    static {
        firstLineButtons.add(MAIN_MENU_BUTTON);

        replyButtonLines.add(firstLineButtons);
    }

    @Override
    protected CommandName getType() {
        return CommandName.ABOUT_US;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendMessage sendMessage = getSendMessageWithTelegramDataAndReplyKeyboard(update, TELEGRAM_DATA, replyButtonLines);

        livingWageBot.execute(sendMessage);
    }
}
