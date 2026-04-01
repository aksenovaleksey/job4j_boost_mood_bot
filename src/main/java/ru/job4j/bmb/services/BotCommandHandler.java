package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.Optional;

@Service
public class BotCommandHandler {

    private final UserRepository userRepository;
    private final MoodService moodService;
    private final TgUI tgUI;
    private final DailyAdviceService dailyAdviceService;

    public BotCommandHandler(UserRepository userRepository,
                             MoodService moodService,
                             TgUI tgUI,
                             DailyAdviceService dailyAdviceService) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
        this.dailyAdviceService = dailyAdviceService;
    }

    public Optional<Content> commands(Message message) {
        if (message == null || !message.hasText()) {
            return Optional.empty();
        }

        String text = message.getText().trim();
        long chatId = message.getChatId();
        Long clientId = message.getFrom().getId();

        return switch (text) {
            case "/start" -> handleStartCommand(chatId, clientId);
            case "/week_mood_log" -> moodService.weekMoodLogCommand(chatId, clientId);
            case "/month_mood_log" -> moodService.monthMoodLogCommand(chatId, clientId);
            case "/award" -> moodService.awards(chatId, clientId);
            case "/daily_advice" -> Optional.of(dailyAdviceService.getDailyAdvice(chatId, clientId));
            case "/settings" -> Optional.of(dailyAdviceService.getSettings(chatId, clientId));
            case "/enable_advice" -> Optional.of(dailyAdviceService.toggleDailyAdvice(chatId, clientId, true));
            case "/disable_advice" -> Optional.of(dailyAdviceService.toggleDailyAdvice(chatId, clientId, false));
            default -> Optional.empty();
        };
    }

    public Optional<Content> handleCallback(CallbackQuery callback) {
        Long moodId;
        try {
            moodId = Long.valueOf(callback.getData());
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        var user = userRepository.findByChatIdAndClientId(
                callback.getMessage().getChatId(),
                callback.getFrom().getId()
        );

        return user.map(value -> moodService.chooseMood(value, moodId));
    }

    public Optional<Content> receive(Content content) {

        return Optional.empty();
    }

    private Optional<Content> handleStartCommand(long chatId, Long clientId) {
        var user = new User();
        user.setClientId(clientId);
        user.setChatId(chatId);
        userRepository.save(user);

        var content = new Content(user.getChatId());
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }
}