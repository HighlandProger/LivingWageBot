package ru.rusguardian.service.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import ru.rusguardian.domain.TelegramDataDto;
import ru.rusguardian.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TelegramDataServiceImplTest {

    private static final String DATA_FILE_PATH = "/data/telegram_data.json";
    private final List<TelegramDataDto> telegramDataList = new ArrayList<>();
    private static final int TELEGRAM_DATA_LIST_SIZE = 29;

    @Test
    void initData() {
        String text = FileUtils.getTextFromResourcesFile(DATA_FILE_PATH);
        JsonArray data = JsonParser.parseString(text).getAsJsonArray();
        for (JsonElement element : data) {
            JsonObject object = element.getAsJsonObject();
            TelegramDataDto telegramData = new TelegramDataDto();
            telegramData.setName(object.get("name").getAsString());
            telegramData.setTextMessage(object.get("textMessage").getAsString());
            telegramData.setPhotoId(object.get("photoId").getAsString());
            telegramData.setStickerId(object.get("stickerId").getAsString());

            telegramDataList.add(telegramData);
        }

        assertEquals(TELEGRAM_DATA_LIST_SIZE, telegramDataList.size());
    }
}