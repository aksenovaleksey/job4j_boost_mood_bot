package ru.job4j.bmb.services;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.event.MoodSelectedEvent;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementService {

    private final MoodLogRepository moodLogRepository;
    private final AchievementRepository achievementRepository;
    private final AwardRepository awardRepository;
    private final SentContent sentContent;

    public AchievementService(MoodLogRepository moodLogRepository,
                              AchievementRepository achievementRepository,
                              AwardRepository awardRepository,
                              SentContent sentContent) {
        this.moodLogRepository = moodLogRepository;
        this.achievementRepository = achievementRepository;
        this.awardRepository = awardRepository;
        this.sentContent = sentContent;
    }

    @Async
    @EventListener
    @Transactional
    public void onMoodSelected(MoodSelectedEvent event) {
        User user = event.getUser();
        awardAchievementsIfEligible(user);
    }

    private void awardAchievementsIfEligible(User user) {
        List<MoodLog> allLogs = moodLogRepository.findByUser(user);

        long positiveDays = allLogs.stream()
                .filter(log -> log.getMood() != null && Boolean.TRUE.equals(log.getMood().getIsPositive()))
                .map(MoodLog::getCreatedAt)
                .distinct()
                .count();

        if (positiveDays == 0) {
            return;
        }

        List<Award> allAwards = awardRepository.findAll();
        List<Achievement> userAchievements = achievementRepository.findByUser(user);
        List<Long> awardedAwardIds = userAchievements.stream()
                .map(a -> a.getAward().getId())
                .collect(Collectors.toList());

        for (Award award : allAwards) {
            if (awardedAwardIds.contains(award.getId())) {
                continue;
            }

            if (positiveDays >= award.getDaysRequired()) {
                Achievement newAchievement = new Achievement();
                newAchievement.setUser(user);
                newAchievement.setAward(award);
                newAchievement.setAwardedAt(Instant.now().getEpochSecond());
                achievementRepository.save(newAchievement);

                Content notification = new Content(user.getChatId());
                notification.setText("🎉 Поздравляем! Вы получили достижение:\n\n" +
                        "🏆 " + award.getName() + "\n" +
                        "📝 " + award.getDescription());
                sentContent.sent(notification);
            }
        }
    }
}