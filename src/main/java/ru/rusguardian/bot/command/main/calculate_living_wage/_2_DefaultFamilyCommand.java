package ru.rusguardian.bot.command.main.calculate_living_wage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.TelegramDataEnum;
import ru.rusguardian.service.data.RegionLivingWageServiceImpl;
import ru.rusguardian.util.TelegramEditMessageUtils;
import ru.rusguardian.util.TelegramUtils;

import static ru.rusguardian.domain.TelegramDataEnum.SET_FAMILY;

@Component
public class _2_DefaultFamilyCommand extends Command {

    private static final TelegramDataEnum TELEGRAM_DATA = SET_FAMILY;

    @Autowired
    private RegionLivingWageServiceImpl regionServiceImpl;

    @Override
    protected CommandName getType() {
        return CommandName.DEFAULT_FAMILY;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        SendMessage sendMessage = getSetFamilySendMessage(update);
        livingWageBot.execute(sendMessage);
    }

    private SendMessage getSetFamilySendMessage(Update update) {
        String message = TELEGRAM_DATA.getTextMessage();
        SendMessage sendMessage = getSimpleSendMessage(update, message);
        sendMessage.setReplyMarkup(getSetFamilyInlineKeyboard(update));

        return sendMessage;
    }

    private InlineKeyboardMarkup getSetFamilyInlineKeyboard(Update update) {
        Chat chat = chatService.findById(TelegramUtils.getChatId(update));
        return TelegramEditMessageUtils.getFamilyCountInlineKeyboard(chat);
    }
}
