package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class DailyAdviceService {

    private static final List<String> POSITIVE_ADVICE = List.of(
            "🌟 Сегодня отличный день для новых начинаний!",
            "💪 Ваша энергия вдохновляет других — продолжайте в том же духе!",
            "✨ Маленькие шаги сегодня приведут к большим победам завтра!",
            "🎯 Сфокусируйтесь на одной важной задаче и завершите её!",
            "☀️ Улыбнитесь миру — он ответит вам тем же!",
            "🚀 Вы способны на большее, чем думаете. Доверяйте себе!",
            "🌈 Каждый день — это новая возможность стать лучше!"
    );

    private static final List<String> SUPPORTIVE_ADVICE = List.of(
            "🤗 Всё будет хорошо. Вы сильнее, чем думаете.",
            "🌿 Сделайте глубокий вдох. Этот момент тоже пройдёт.",
            "💙 Позвольте себе отдохнуть — забота о себе важна.",
            "🫂 Вы не одни. Обратитесь к близким за поддержкой.",
            "🕯️ Даже в темноте есть свет. Найдите его в мелочах.",
            "🧘‍♀️ Практикуйте благодарность: найдите 3 вещи, за которые можно сказать «спасибо».",
            "💫 Не требуйте от себя совершенства. Достаточно просто быть."
    );

    private static final List<String> NEUTRAL_ADVICE = List.of(
            "📚 Уделите 15 минут чтению или обучению новому.",
            "🚶‍♂️ Прогулка на свежем воздухе улучшит ваше самочувствие.",
            "💧 Не забывайте пить воду в течение дня.",
            "📝 Запишите 3 цели на сегодня — это поможет сфокусироваться.",
            "🎵 Включите любимую музыку для поднятия настроения.",
            "🤝 Сделайте маленькое доброе дело — это поднимет настроение вам и другим.",
            "🧹 Наведите порядок на рабочем столе — порядок вокруг помогает порядку внутри."
    );

    private final SentContent sentContent;
    private final UserRepository userRepository;
    private final MoodLogRepository moodLogRepository;
    private final Random random = new Random();

    public DailyAdviceService(SentContent sentContent,
                              UserRepository userRepository,
                              MoodLogRepository moodLogRepository) {
        this.sentContent = sentContent;
        this.userRepository = userRepository;
        this.moodLogRepository = moodLogRepository;
    }

    public Content getDailyAdvice(Long chatId, Long clientId) {
        User user = userRepository.findByChatIdAndClientId(chatId, clientId)
                .orElseThrow(() -> new RuntimeException("User not found: chatId=" + chatId));

        String advice = generateAdviceForUser(user);

        user.setLastAdviceDate(Instant.now().getEpochSecond());
        userRepository.save(user);

        return new Content(chatId).setText("💡 Совет дня:\n\n" + advice);
    }

    private String generateAdviceForUser(User user) {
        Boolean moodSentiment = getCurrentMoodSentiment(user);

        List<String> adviceList;
        if (moodSentiment == null) {
            adviceList = NEUTRAL_ADVICE;
        } else if (Boolean.TRUE.equals(moodSentiment)) {
            adviceList = POSITIVE_ADVICE;
        } else {
            adviceList = SUPPORTIVE_ADVICE;
        }

        return adviceList.get(random.nextInt(adviceList.size()));
    }

    private Boolean getCurrentMoodSentiment(User user) {
        List<MoodLog> logs = moodLogRepository.findByUser(user);
        if (logs.isEmpty()) {
            return null;
        }
        MoodLog latest = logs.stream()
                .max(Comparator.comparingLong(MoodLog::getCreatedAt))
                .orElse(null);
        return latest != null && latest.getMood() != null
                ? latest.getMood().getIsPositive()
                : null;
    }

    public boolean canSendAutoAdvice(User user) {
        if (!user.isDailyAdviceEnabled()) {
            return false;
        }

        Long lastDate = user.getLastAdviceDate();
        if (lastDate == null) {
            return true;
        }

        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        LocalDate lastAdviceDate = Instant.ofEpochSecond(lastDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return !today.isEqual(lastAdviceDate);
    }

    @Scheduled(cron = "${daily.advice.cron:0 9 * * *}")
    public void sendDailyAdviceToAll() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (canSendAutoAdvice(user)) {
                String advice = generateAdviceForUser(user);
                Content content = new Content(user.getChatId())
                        .setText("🌅 Доброе утро! Совет дня:\n\n" + advice);
                sentContent.sent(content);

                user.setLastAdviceDate(Instant.now().getEpochSecond());
                userRepository.save(user);
            }
        }
    }

    public Content toggleDailyAdvice(Long chatId, Long clientId, boolean enable) {
        User user = userRepository.findByChatIdAndClientId(chatId, clientId)
                .orElseThrow(() -> new RuntimeException("User not found: chatId=" + chatId));

        user.setDailyAdviceEnabled(enable);
        userRepository.save(user);

        String message = enable
                ? "✅ Автоматические советы дня включены!\nВы будете получать их каждое утро."
                : "❌ Автоматические советы дня выключены.\nВы всегда можете запросить совет вручную командой /daily_advice";

        return new Content(chatId).setText(message);
    }

    public Content getSettings(Long chatId, Long clientId) {
        User user = userRepository.findByChatIdAndClientId(chatId, clientId)
                .orElseThrow(() -> new RuntimeException("User not found: chatId=" + chatId));

        String status = user.isDailyAdviceEnabled() ? "✅ Включено" : "❌ Выключено";
        String text = "⚙️ Настройки бота:\n\n" +
                "📬 Автоматические советы: " + status + "\n\n" +
                "Управление:\n" +
                "/enable_advice — включить авто-советы\n" +
                "/disable_advice — выключить авто-советы\n" +
                "/daily_advice — получить совет вручную";

        return new Content(chatId).setText(text);
    }
}
