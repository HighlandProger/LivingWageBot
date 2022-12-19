package ru.rusguardian.bot.command.service;

public enum CommandName {

    START("/start"),
    ADMIN("⭕️Администратору"),
    NOT_FOUND("/notFound"),
    EMPTY("/empty"),
    MAIN_MENU("\uD83C\uDFE0В главное меню"),
    //ADMIN----------------------------------------------------------------------------------
    SET_ACTION("Отправить акцию"),
    SEND_ACTION("/sendAction"),
    SET_ABOUT_US("Изменить \"О нас\""),
    SEND_ABOUT_US("/sendAboutUs"),
    CONFIRM_ACTION("/confirmAction"),
    CONFIRM_ABOUT_US("/confirmAboutUs"),
    //START----------------------------------------------------------------------------------
    CATALOG("\uD83D\uDECDКаталог"),
    BASKET("\uD83D\uDED2Корзина"),
    CONTACTS("\uD83D\uDCF1Контакты"),
    ABOUT_US("ℹ️О нас"),
    STOCKS("\uD83C\uDF81Акции"),
    YOUTUBE_LINK("\uD83D\uDCF9You Tube"),
    CALCULATE_LIVING_WAGE("\uD83D\uDCA1Посчитать прожиточный минимум"),
    CLIENT_CHAT("\uD83D\uDC68\u200D\uD83D\uDCBBКлиентский чат"),
//-----------------------------------------------------------------------------------------

    //   CATALOG
    PREPARED_BUSINESS_PLAN("Готовый Бизнес-План"),
    INDIVIDUAL_BUSINESS_PLAN("Индивидуальный Бизнес-План"),
    FAVOURITES("⭐️Избранное"),

    //   CONTACTS
    WRITE_QUESTION("\uD83D\uDCAC️Написать"),
    CALL_QUESTION("\uD83D\uDCDEПозвонить"),
    AVITO("\uD83D\uDC8EАвито"),
    SITE("\uD83C\uDF10Сайт"),

    //   ABOUT_US
    ABOUT_US_COMPANY("\uD83D\uDCCDО компании"),
    ABOUT_US_REQUISITES("\uD83D\uDCCBРеквизиты"),

    //   STOCKS
    ALL_STOCKS("💥Все акции"),

    //   CALCULATE_LIVING_WAGE
    REGION_NAME("/regionName"),
    DEFAULT_FAMILY("/defaultFamily"),
    CHANGE_FAMILY("/changeFamily"),
    CONFIRM_FAMILY("/confirmFamily"),
    SETTING_SALARIES_QUESTION("/settingSalariesQuestion"),
    SETTING_SALARIES_ANSWER("/settingSalariesAnswer"),
    RESULT("/resultCommand"),

    // EXTRA
    CLIENT_MESSAGE("/clientMessage");


    private final String name;

    CommandName(String commandName) {
        this.name = commandName;
    }

    public String getName() {
        return this.name;
    }
}
