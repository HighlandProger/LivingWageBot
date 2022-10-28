package ru.rusguardian.bot.command.main.start.contacts;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;
import ru.rusguardian.bot.command.service.SendMessageService;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.util.TelegramUtils;

import static ru.rusguardian.domain.TelegramDataEnum.SITE;

@Component
public class SiteCommand extends Command implements SendMessageService {

    private static final TelegramDataEnum TELEGRAM_DATA = SITE;

    @Override
    public Command getCommand() {
        return this;
    }

    @Override
    protected CommandName getType() {
        return CommandName.SITE;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendMessage sendMessage = getFormattedSendMessage(update);
        livingWageBot.execute(sendMessage);
    }

    private SendMessage getFormattedSendMessage(Update update) {
        String formattedMessage = String.format(TELEGRAM_DATA.getTextMessage(), TelegramUtils.getFirstname(update));
        return getSimpleSendMessage(update, formattedMessage);
    }
}
