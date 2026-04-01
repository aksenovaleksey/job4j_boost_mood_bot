package ru.job4j.bmb.content;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ContentProviderText implements ContentProvider {

    private static final Map<Long, String> TEXT_CONTENT = Map.ofEntries(
            Map.entry(1L, "Ты не один! Всё наладится 🤝"),
            Map.entry(2L, "Сделай глубокий вдох. Ты справишься 💪"),
            Map.entry(3L, "Маленькие шаги ведут к большим победам ✨"),
            Map.entry(4L, "Кофе, музыка, перерыв — и снова в бой ☕"),
            Map.entry(5L, "Отдых — это тоже часть продуктивности 😴")
    );

    @Override
    public Content byMood(Long chatId, Long moodId) {
        String text = TEXT_CONTENT.getOrDefault(moodId, "Держись! Всё будет хорошо 🌟");
        return new Content(chatId).setText(text);
    }
}