package ru.rusguardian.service.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rusguardian.domain.TelegramDataDto;
import ru.rusguardian.util.FileUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class TelegramDataServiceImpl {

    private static final String DATA_FILE_PATH = "/data/telegram_data.json";
    private final List<TelegramDataDto> telegramDataList = new ArrayList<>();

    @PostConstruct
    private void initData() {
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
    }

    public TelegramDataDto getTelegramDataByName(String name) {
        Optional<TelegramDataDto> telegramDataOptional = telegramDataList.stream().filter(s -> s.getName().equals(name)).findFirst();
        if (telegramDataOptional.isEmpty()) {
            log.error("Telegram data with name {} not found", name);
            throw new NoSuchElementException();
        }
        return telegramDataOptional.get();
    }

    public List<TelegramDataDto> getTelegramData() {
        return this.telegramDataList;
    }

}
