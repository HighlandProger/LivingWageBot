package ru.rusguardian.bot.command.main.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Status;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.service.data.exception.EntityNotFoundException;

import static ru.rusguardian.domain.TelegramDataEnum.SET_REGION;

@Component
@Slf4j
public class CalculateLivingWageCommand extends Command {

    private static final TelegramDataEnum TELEGRAM_DATA = SET_REGION;

    @Override
    protected CommandName getType() {
        return CommandName.CALCULATE_LIVING_WAGE;
    }

    @Override
    public void mainExecute(Update update) throws TelegramApiException {

        setUserStatusSettingRegion(update);

        SendMessage sendMessage = getSendMessage(update);

        livingWageBot.execute(sendMessage);
    }

    private void setUserStatusSettingRegion(Update update) {
        try {
            changeUserStatus(update, Status.SETTING_REGION);
        } catch (EntityNotFoundException e) {
            createDefaultUser(update);
            changeUserStatus(update, Status.SETTING_REGION);
        }
    }

    private SendMessage getSendMessage(Update update) {
        String callbackMessage = getCallbackMessage();
        return getSimpleSendMessage(update, callbackMessage);
    }

    private String getCallbackMessage() {
        return TELEGRAM_DATA.getTextMessage();
    }

}