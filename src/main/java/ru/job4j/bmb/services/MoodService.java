package ru.job4j.bmb.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.event.MoodSelectedEvent;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.MoodRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoodService {
    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final AwardRepository awardRepository;
    private final MoodRepository moodRepository;
    private final ApplicationEventPublisher eventPublisher;

    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       AwardRepository awardRepository,
                       MoodRepository moodRepository,
                       ApplicationEventPublisher eventPublisher) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.awardRepository = awardRepository;
        this.moodRepository = moodRepository;
        this.eventPublisher = eventPublisher;
    }

    public Content chooseMood(User user, Long moodId) {
        Mood mood = moodRepository.findById(moodId)
                .orElse(new Mood("Неизвестно", null));

        MoodLog log = new MoodLog();
        log.setUser(user);
        log.setMood(mood);
        log.setCreatedAt(Instant.now().getEpochSecond());
        moodLogRepository.save(log);

        eventPublisher.publishEvent(new MoodSelectedEvent(this, user, moodId));

        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        return getUserAndLogs(chatId, clientId, 7, "📊 Настроение за неделю");
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        return getUserAndLogs(chatId, clientId, 30, "📈 Настроение за месяц");
    }

    private Optional<Content> getUserAndLogs(long chatId, Long clientId, int days, String title) {
        User user = userRepository.findByChatIdAndClientId(chatId, clientId)
                .orElse(null);
        if (user == null) {
            return Optional.of(new Content(chatId).setText("❌ Пользователь не найден"));
        }

        Instant periodAgo = Instant.now().minus(days, ChronoUnit.DAYS);
        List<MoodLog> logs = moodLogRepository
                .findByUserAndCreatedAtAfter(user, periodAgo.getEpochSecond())
                .stream()
                .sorted((l1, l2) -> Long.compare(l2.getCreatedAt(), l1.getCreatedAt()))
                .collect(Collectors.toList());

        String formatted = formatMoodLogs(logs, title);
        return Optional.of(new Content(chatId).setText(formatted));
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\n📭 Записей о настроении не найдено.";
        }
        var sb = new StringBuilder(title).append(":\n\n");
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            String moodText = log.getMood() != null && log.getMood().getName() != null
                    ? log.getMood().getName()
                    : "Неизвестно";
            sb.append("🕐 ").append(formattedDate).append(": ").append(moodText).append("\n");
        });
        return sb.toString();
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        User user = userRepository.findByChatIdAndClientId(chatId, clientId)
                .orElse(null);
        if (user == null) {
            return Optional.of(new Content(chatId).setText("❌ Пользователь не найден"));
        }

        List<Achievement> achievements = achievementRepository.findByUser(user);
        if (achievements.isEmpty()) {
            return Optional.of(new Content(chatId).setText(
                    "🏆 У вас пока нет наград.\n\n" +
                            "💡 Поддерживайте хорошее настроение, и вы получите первые достижения!"
            ));
        }

        var sb = new StringBuilder("🏆 Ваши награды:\n\n");
        achievements.forEach(achievement -> {
            sb.append("✅ ").append(achievement.getName()).append("\n");
        });

        return Optional.of(new Content(chatId).setText(sb.toString()));
    }
}