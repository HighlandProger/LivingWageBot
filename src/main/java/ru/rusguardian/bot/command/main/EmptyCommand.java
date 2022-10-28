package ru.rusguardian.bot.command.main;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rusguardian.bot.command.service.Command;
import ru.rusguardian.bot.command.service.CommandName;

@Component
public class EmptyCommand extends Command {

    @Override
    protected void mainExecute(Update update) {

        //EmptyCommand does nothing
    }

    @Override
    protected CommandName getType() {
        return CommandName.EMPTY;
    }

}
