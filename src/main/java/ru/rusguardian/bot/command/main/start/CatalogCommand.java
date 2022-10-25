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

import static ru.rusguardian.domain.TelegramDataEnum.CATALOG;

@Component
public class CatalogCommand extends Command {

    private static final TelegramDataEnum TELEGRAM_DATA = CATALOG;

    private static final List<String> buttonLines = new ArrayList<>();
    private static final String PREPARED_BUSINESS_PLAN_BUTTON = "Готовый Бизнес-План";
    private static final String INDIVIDUAL_BUSINESS_PLAN_BUTTON = "Индивидуальный Бизнес-План";

    static {
        buttonLines.add(PREPARED_BUSINESS_PLAN_BUTTON);
        buttonLines.add(INDIVIDUAL_BUSINESS_PLAN_BUTTON);
    }

    @Override
    protected CommandName getType() {
        return CommandName.CATALOG;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendMessage sendMessage = getSendMessageWithTelegramDataAndInlineKeyboard(update, TELEGRAM_DATA, buttonLines);

        livingWageBot.execute(sendMessage);
    }
}