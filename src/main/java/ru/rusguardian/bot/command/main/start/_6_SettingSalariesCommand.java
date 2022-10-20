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
import ru.rusguardian.util.DateUtils;
import ru.rusguardian.util.TelegramUtils;

import java.util.List;

@Component
public class _6_SettingSalariesCommand extends Command {

    private static final String MONTH_SALARY_QUESTION = "Пожалуйста, введите общий доход Вашей семьи за %s г.";

    @Override
    protected CommandName getType() {
        return CommandName.SETTING_SALARIES;
    }

    @Override
    protected void mainExecute(Update update) throws TelegramApiException {

        Chat chat = chatService.findById(TelegramUtils.getChatId(update));
        String monthSalaryQuestion = getMonthSalaryQuestion(chat);
        SendMessage sendMessage = getSimpleSendMessage(update, monthSalaryQuestion);

        livingWageBot.execute(sendMessage);
        chat.setStatus(Status.SETTING_SALARIES);
        chatService.updateChat(chat);
    }

    private String getMonthSalaryQuestion(Chat chat) {
        List<Integer> clientSalaries = chat.getSalaries();
        int monthsToRoll = 3 - clientSalaries.size();

        String monthAndYear = DateUtils.getRolledFormattedDate(monthsToRoll);
        return String.format(MONTH_SALARY_QUESTION, monthAndYear);
    }

    private int getLivingWagesSum(Chat chat) {

        RegionLivingWage regionLivingWage = chat.getRegionLivingWage();
        int employeesSum = chat.getEmployeeCount() * regionLivingWage.getEmployeeLivingWage();
        int childrenSum = chat.getChildCount() * regionLivingWage.getChildLivingWage();
        int retireeSum = chat.getRetireeCount() * regionLivingWage.getRetireeLivingWage();

        return employeesSum + childrenSum + retireeSum;
    }

}
