package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.util.TelegramUtils;

import java.util.List;

@Component
public class ResultCommand extends Command {

    private static final String SUCCESS_CLIENT_TELEGRAM_DATA = "SUCCESS_CLIENT";
    private static final String NOT_SUCCESS_CLIENT_TELEGRAM_DATA = "NOT_SUCCESS_CLIENT";

    @Override
    protected CommandName getType() {
        return CommandName.RESULT;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        Chat chat = chatService.findById(TelegramUtils.getChatId(update));

        SendMessage sendMessage = getSuccessSendMessage(update);

        if (!isSuitable(chat)) {
            sendMessage = getNotSuccessSendMessage(update);
        }

        livingWageBot.execute(sendMessage);

        chat.getSalaries().clear();
        chatService.updateChat(chat);
    }

    private SendMessage getSuccessSendMessage(Update update) {
        String message = telegramDataService.getTelegramDataByName(SUCCESS_CLIENT_TELEGRAM_DATA).getTextMessage();
        return getSimpleSendMessage(update, message);
    }

    private SendMessage getNotSuccessSendMessage(Update update) {
        String message = telegramDataService.getTelegramDataByName(NOT_SUCCESS_CLIENT_TELEGRAM_DATA).getTextMessage();
        return getSimpleSendMessage(update, message);
    }

    private boolean isSuitable(Chat chat) {
        int resultLivingWage = getLivingWagesSum(chat);
        List<Integer> salaries = chat.getSalaries();
        return salaries.stream().filter(e -> e > resultLivingWage).findFirst().isEmpty();
    }

    private int getLivingWagesSum(Chat chat) {

        RegionLivingWage regionLivingWage = chat.getRegionLivingWage();
        int employeesSum = chat.getEmployeeCount() * regionLivingWage.getEmployeeLivingWage();
        int childrenSum = chat.getChildCount() * regionLivingWage.getChildLivingWage();
        int retireeSum = chat.getRetireeCount() * regionLivingWage.getRetireeLivingWage();

        return employeesSum + childrenSum + retireeSum;
    }
}
