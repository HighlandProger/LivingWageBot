package ru.rusguardian.bot.command.main.start.catalog;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.TelegramDataEnum;

import java.util.ArrayList;
import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.PREPARED_BUSINESS_PLAN;

@Component
public class PreparedBusinessPlanButton extends Command {

    private static final TelegramDataEnum TELEGRAM_DATA = PREPARED_BUSINESS_PLAN;

    private static final List<List<String>> replyButtonLines = new ArrayList<>();
    private static final List<String> firstLineButtons = new ArrayList<>();

    private static final String MAIN_MENU_BUTTON = "В главное меню";

    static {
        firstLineButtons.add(MAIN_MENU_BUTTON);

        replyButtonLines.add(firstLineButtons);
    }

    @Override
    protected CommandName getType() {
        return CommandName.PREPARED_BUSINESS_PLAN;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {
        SendMessage sendMessage = getSendMessageWithTelegramDataAndReplyKeyboard(update, TELEGRAM_DATA, replyButtonLines);

        livingWageBot.execute(sendMessage);
    }
}
