package ru.job4j.bmb.services;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TgResponseHandler {

    private static final Map<Long, String> MOOD_RESPONSES = Map.ofEntries(
            Map.entry(1L, "Невероятно! Вы сияете от счастья, продолжайте радоваться жизни. ✨"),
            Map.entry(2L, "Великолепно! Вы чувствуете себя на высоте. Продолжайте в том же духе. 🌟"),
            Map.entry(3L, "Потрясающе! Вы в состоянии внутреннего мира и гармонии. 🧘"),
            Map.entry(4L, "Отлично! Вы чувствуете себя уютно и спокойно. ☺️"),
            Map.entry(5L, "Замечательно! Немного волнения добавляет жизни краски. 🎈"),
            Map.entry(6L, "Хорошо! Ваш фокус на высоте, используйте это время эффективно. 🎯"),
            Map.entry(7L, "Не волнуйтесь, всё пройдет. Попробуйте расслабиться и выдохнуть. 🌬️"),
            Map.entry(8L, "Бывает. Не позволяйте разочарованию сбить вас с толку, всё наладится. 💙"),
            Map.entry(9L, "Похоже, вам нужен отдых. Позаботьтесь о себе и отдохните. 😴"),
            Map.entry(10L, "Потрясающе! Вы полны идей и энергии для их реализации. 💡"),
            Map.entry(11L, "Попробуйте успокоиться и найти причину раздражения, чтобы исправить ситуацию. 🧘‍♂️")
    );

    public String getResponse(Long moodId) {
        return MOOD_RESPONSES.getOrDefault(moodId,
                "Спасибо, что поделились настроением! 🌟\nМы записали ваш выбор.");
    }
}