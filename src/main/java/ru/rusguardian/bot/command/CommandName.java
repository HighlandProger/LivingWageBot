package ru.rusguardian.bot.command;

public enum CommandName {

    START("/start"),
    NOT_FOUND("/notFound"),
    EMPTY("/empty"),

    CHOOSE_REGION("Выбрать регион"),
    REGION_NAME("/regionName"),
    DEFAULT_FAMILY("/defaultFamily"),
    CHANGE_FAMILY("/changeFamily"),
    CONFIRM_FAMILY("/confirmFamily"),
    SETTING_SALARIES("/settingSalaries"),
    ANSWER("/answer");

    private final String name;

    CommandName(String commandName) {
        this.name = commandName;
    }

    public String getName() {
        return this.name;
    }
}