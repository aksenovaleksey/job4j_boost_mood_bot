package ru.job4j.bmb.content;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

@Component
public class ContentProviderAudio implements ContentProvider {

    private static final Map<Long, String> AUDIO_PATHS = Map.ofEntries(
            Map.entry(1L, "audio/supportive.mp3"),
            Map.entry(2L, "audio/calm_melody.mp3"),
            Map.entry(3L, "audio/energetic_beat.mp3"),
            Map.entry(4L, "audio/coffee_jazz.mp3"),
            Map.entry(5L, "audio/relaxing_night.mp3")
    );

    @Override
    public Content byMood(Long chatId, Long moodId) {
        String path = AUDIO_PATHS.getOrDefault(moodId, "audio/default.mp3");
        try {
            var resource = new ClassPathResource(path);
            if (!resource.exists()) {
                return new Content(chatId).setAudio(
                        new InputFile(new ByteArrayInputStream(new byte[1]), path)
                );
            }
            return new Content(chatId).setAudio(
                    new InputFile(resource.getInputStream(), path)
            );
        } catch (IOException e) {
            return new Content(chatId).setAudio(
                    new InputFile(new ByteArrayInputStream(new byte[1]), path)
            );
        }
    }
}