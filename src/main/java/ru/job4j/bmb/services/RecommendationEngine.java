package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.ContentProvider;

import java.util.List;
import java.util.Random;

@Service
public class RecommendationEngine {

    private final List<ContentProvider> contentProviders;
    private static final Random RND = new Random();

    public RecommendationEngine(List<ContentProvider> contentProviders) {
        this.contentProviders = contentProviders;
    }

    public Content recommendFor(Long chatId, Long moodId) {
        if (contentProviders.isEmpty()) {
            return new Content(chatId).setText("⚠️ Нет доступных поставщиков контента");
        }
        int index = RND.nextInt(contentProviders.size());
        return contentProviders.get(index).byMood(chatId, moodId);
    }

    public Content recommendByType(Long chatId, Long moodId, Class<? extends ContentProvider> providerType) {
        return contentProviders.stream()
                .filter(p -> providerType.isAssignableFrom(p.getClass()))
                .findFirst()
                .map(p -> p.byMood(chatId, moodId))
                .orElse(new Content(chatId).setText("⚠️ Тип контента недоступен"));
    }
}
