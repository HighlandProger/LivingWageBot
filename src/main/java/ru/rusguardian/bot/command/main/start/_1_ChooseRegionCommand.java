package ru.rusguardian.bot.command.main.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Status;

@Component
@Slf4j
public class _1_ChooseRegionCommand extends Command {

    private static final String TELEGRAM_DATA_NAME = "SET REGION";

    @Override
    protected CommandName getType() {
        return CommandName.CHOOSE_REGION;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        changeUserStatus(update, Status.SETTING_REGION);

        SendMessage sendMessage = getSendMessage(update);

        livingWageBot.execute(sendMessage);
    }

    private SendMessage getSendMessage(Update update) {
        String callbackMessage = getCallbackMessage();
        return getSimpleSendMessage(update, callbackMessage);
    }

    private String getCallbackMessage() {
        return telegramDataService.getTelegramDataByName(TELEGRAM_DATA_NAME).getTextMessage();
    }

}