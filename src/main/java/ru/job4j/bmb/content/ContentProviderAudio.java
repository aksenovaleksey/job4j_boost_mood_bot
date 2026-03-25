package ru.job4j.bmb.content;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import java.io.File;
import java.util.Map;

@Component
public class ContentProviderAudio implements ContentProvider {

    private static final Map<Long, String> AUDIO_PATHS = Map.ofEntries(
            Map.entry(1L, "./audio/supportive.mp3"),
            Map.entry(2L, "./audio/calm_melody.mp3"),
            Map.entry(3L, "./audio/energetic_beat.mp3"),
            Map.entry(4L, "./audio/coffee_jazz.mp3"),
            Map.entry(5L, "./audio/relaxing_night.mp3")
    );

    @Override
    public Content byMood(Long chatId, Long moodId) {
        String path = AUDIO_PATHS.getOrDefault(moodId, "./audio/default.mp3");
        File audioFile = new File(path);

        if (!audioFile.exists()) {
            return new Content(chatId).setText("🎵 Аудио временно недоступно");
        }

        return new Content(chatId).setAudio(new InputFile(audioFile));
    }
}
