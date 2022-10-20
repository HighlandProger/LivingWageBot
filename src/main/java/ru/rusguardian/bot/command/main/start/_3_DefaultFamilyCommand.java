package ru.rusguardian.bot.command.main.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.service.data.RegionLivingWageServiceImpl;
import ru.rusguardian.util.TelegramEditMessageUtils;
import ru.rusguardian.util.TelegramUtils;

@Component
public class _3_DefaultFamilyCommand extends Command {

    private static final String MESSAGE = "Теперь введите свой состав семьи и нажмите кнопку \"Подтвердить,\"";

    @Autowired
    private RegionLivingWageServiceImpl regionServiceImpl;

    @Override
    protected CommandName getType() {
        return CommandName.DEFAULT_FAMILY;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        Chat chat = chatService.findById(TelegramUtils.getChatId(update));

        SendMessage sendMessage = getSimpleSendMessage(update, MESSAGE);
        sendMessage.setReplyMarkup(TelegramEditMessageUtils.getFamilyCountInlineKeyboard(chat));
        sendMessage.setParseMode("HTML");

        livingWageBot.execute(sendMessage);
    }

}
