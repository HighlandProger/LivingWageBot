package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.TelegramDataEnum;

import java.util.List;

import static ru.rusguardian.domain.TelegramDataEnum.BASKET_MESSAGE;

@Component
public class BasketCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = BASKET_MESSAGE;

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.BASKET;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        List<List<String>> replyButtonLines = getMainMenuReplyButtonLine();
        SendMessage sendMessage = getSendMessageWithTelegramDataAndReplyKeyboard(update, TELEGRAM_DATA, replyButtonLines);

        livingWageBot.execute(sendMessage);
    }


}
