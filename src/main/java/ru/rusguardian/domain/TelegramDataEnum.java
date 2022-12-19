package ru.rusguardian.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rusguardian.service.data.TelegramDataServiceImpl;

import javax.annotation.PostConstruct;
import java.util.EnumSet;

public enum TelegramDataEnum {

    WELCOME,
    NOT_FOUND,
    SET_REGION,
    ERROR_REGION_NAME,
    SUCCESS_REGION_NAME,
    SET_FAMILY,
    NEGATIVE_COUNT,
    SALARY_QUESTION,
    MONTH_SALARY_QUESTION,
    DATA_ACCEPTED,
    DATA_DECLINED,
    SUCCESS_CLIENT,
    NOT_SUCCESS_CLIENT,
    BASKET_MESSAGE,
    CALL_QUESTION,
    WRITE_QUESTION,
    CATALOG,
    STOCKS,
    ALL_STOCKS,
    YOU_TUBE_LINK,
    PREPARED_BUSINESS_PLAN,
    INDIVIDUAL_BUSINESS_PLAN,
    MAIN_MENU,
    CLIENT_MESSAGE,
    OWNER_MESSAGE,
    CONTACTS,
    AVITO,
    SITE,
    ABOUT_US,
    ABOUT_US_COMPANY,
    ABOUT_US_REQUISITES,
    ADMIN,
    SET_ACTION,
    SET_ABOUT_US,
    SEND_ACTION,
    SEND_ABOUT_US,
    CONFIRM_ACTION,
    CONFIRM_ABOUT_US,
    FAVOURITES,
    CLIENT_CHAT;

    private TelegramDataServiceImpl telegramDataService;

    private String textMessage;
    private String photoId;
    private String stickerId;
    private String videoId;

    TelegramDataEnum() {
        this.textMessage = "";
        this.photoId = "";
        this.stickerId = "";
        this.videoId = "";
    }

    public void setByTelegramDataDto(TelegramDataDto telegramDataDto) {
        this.textMessage = telegramDataDto.getTextMessage();
        this.photoId = telegramDataDto.getPhotoId();
        this.stickerId = telegramDataDto.getStickerId();
        this.videoId = telegramDataDto.getVideoId();
    }

    public String getTextMessage() {
        return this.textMessage;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getStickerId() {
        return stickerId;
    }

    public String getVideoId() {
        return videoId;
    }

    @Component
    public static class ServiceInjector {

        @Autowired
        TelegramDataServiceImpl telegramDataService;

        @PostConstruct
        public void postConstruct() {
            for (TelegramDataEnum telegramDataEnum : EnumSet.allOf(TelegramDataEnum.class)) {
                telegramDataEnum.telegramDataService = telegramDataService;
                telegramDataEnum.textMessage = telegramDataService.getTelegramDataByName(telegramDataEnum.name()).getTextMessage();
                telegramDataEnum.photoId = telegramDataService.getTelegramDataByName(telegramDataEnum.name()).getPhotoId();
                telegramDataEnum.stickerId = telegramDataService.getTelegramDataByName(telegramDataEnum.name()).getStickerId();
                telegramDataEnum.videoId = telegramDataService.getTelegramDataByName(telegramDataEnum.name()).getVideoId();
            }
        }
    }
}
