package ru.job4j.bmb.content;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import java.io.File;
import java.util.Map;

@Component
public class ContentProviderImage implements ContentProvider {

    private static final Map<Long, String> IMAGE_PATHS = Map.ofEntries(
            Map.entry(1L, "./images/sad_cat.png"),
            Map.entry(2L, "./images/neutral_face.png"),
            Map.entry(3L, "./images/happy_dance.gif"),
            Map.entry(4L, "./images/coffee_time.png"),
            Map.entry(5L, "./images/sleepy_bear.png")
    );

    @Override
    public Content byMood(Long chatId, Long moodId) {
        String path = IMAGE_PATHS.getOrDefault(moodId, "./images/default.png");
        File imageFile = new File(path);

        if (!imageFile.exists()) {
            return new Content(chatId).setText("🖼️ Изображение временно недоступно");
        }

        return new Content(chatId).setPhoto(new InputFile(imageFile));
    }
}