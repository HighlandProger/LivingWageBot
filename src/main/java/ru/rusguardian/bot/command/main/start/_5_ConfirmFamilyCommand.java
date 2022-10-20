package ru.rusguardian.bot.command.main.start;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rusguardian.bot.command.Command;
import ru.rusguardian.bot.command.CommandName;
import ru.rusguardian.domain.Chat;
import ru.rusguardian.domain.RegionLivingWage;
import ru.rusguardian.domain.Status;
import ru.rusguardian.util.TelegramUtils;

@Component
public class _5_ConfirmFamilyCommand extends Command {

    private static final String SALARY_QUESTION = "Отлично! Осталось ввести общий доход Вашей семьи за три последних месяца, не считая текущего";

    @Override
    protected CommandName getType() {
        return CommandName.CONFIRM_FAMILY;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        Chat chat = chatService.findById(TelegramUtils.getChatId(update));
        SendMessage sendMessage = getSimpleSendMessage(update, SALARY_QUESTION);

        livingWageBot.execute(sendMessage);
        chat.setStatus(Status.SETTING_SALARIES);
        chatService.updateChat(chat);
    }

    private int getLivingWagesSum(Chat chat) {

        RegionLivingWage regionLivingWage = chat.getRegionLivingWage();
        int employeesSum = chat.getEmployeeCount() * regionLivingWage.getEmployeeLivingWage();
        int childrenSum = chat.getChildCount() * regionLivingWage.getChildLivingWage();
        int retireeSum = chat.getRetireeCount() * regionLivingWage.getRetireeLivingWage();

        return employeesSum + childrenSum + retireeSum;
    }

}
