package ru.job4j.bmb;

import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.services.BotCommandHandler;
import ru.job4j.bmb.services.TelegramBotService;
import ru.job4j.bmb.services.TgUI;
import ru.job4j.bmb.services.MoodService;
import ru.job4j.bmb.repository.UserRepository;
import ru.job4j.bmb.repository.MoodRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.services.RecommendationEngine;

import java.util.List;

public class DIByDirectInjectMain {
    public static void main(String[] args) {

        System.out.println("⚠️ Ручная инъекция зависимостей без Spring контекста не рекомендуется.");
        System.out.println("✅ Для запуска бота используйте класс Main с Spring Boot.");

        UserRepository userRepository = null;
        MoodRepository moodRepository = null;
        MoodLogRepository moodLogRepository = null;
        AchievementRepository achievementRepository = null;
        AwardRepository awardRepository = null;

        RecommendationEngine recommendationEngine = new RecommendationEngine(List.of());
        MoodService moodService = new MoodService(moodLogRepository, recommendationEngine,
                userRepository, achievementRepository, awardRepository, moodRepository);
        TgUI tgUI = new TgUI(moodRepository);

        var handler = new BotCommandHandler(userRepository, moodService, tgUI);
        var tg = new TelegramBotService(handler);
        tg.receive(new Content(999L).setText("Тестовое сообщение"));
        }
}