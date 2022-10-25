package ru.rusguardian.bot.command;

public enum CommandName {

    START("/start"),
    NOT_FOUND("/notFound"),
    EMPTY("/empty"),
    MAIN_MENU("В главное меню"),

    CATALOG("\uD83D\uDECDКаталог"),
    BASKET("\uD83D\uDED2Корзина"),
    CONTACTS("Контакты"),
    ABOUT_US("О нас"),
    STOCKS("\uD83C\uDF81Акции"),
    YOUTUBE_LINK("\uD83D\uDCF9You Tube"),
    CALCULATE_LIVING_WAGE("\uD83D\uDCA1Посчитать прожиточный минимум"),

    WRITE_QUESTION("✍️Написать"),
    CALL_QUESTION("\uD83D\uDCDEПозвонить"),
    AVITO("Авито"),
    SITE("Сайт"),

    PREPARED_BUSINESS_PLAN("Готовый Бизнес-План"),
    INDIVIDUAL_BUSINESS_PLAN("Индивидуальный Бизнес-План"),

    ALL_STOCKS("Все акции"),

    CLIENT_MESSAGE("/clientMessage"),

    REGION_NAME("/regionName"),
    DEFAULT_FAMILY("/defaultFamily"),
    CHANGE_FAMILY("/changeFamily"),
    CONFIRM_FAMILY("/confirmFamily"),
    SETTING_SALARIES_QUESTION("/settingSalariesQuestion"),
    SETTING_SALARIES_ANSWER("/settingSalariesAnswer"),
    RESULT("/resultCommand");

    private final String name;

    CommandName(String commandName) {
        this.name = commandName;
    }

    public String getName() {
        return this.name;
    }
}
