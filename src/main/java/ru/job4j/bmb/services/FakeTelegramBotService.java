package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.config.condition.FakeModeCondition;
import ru.job4j.bmb.content.Content;

import java.util.Optional;

@Service
@Conditional(FakeModeCondition.class)
public class FakeTelegramBotService extends TelegramLongPollingBot implements SentContent {

    private final BotCommandHandler handler;
    private final String botName;

    public FakeTelegramBotService(
            @Value("${telegram.bot.name}") String botName,
            @Value("${telegram.bot.token}") String botToken,
            BotCommandHandler handler) {
        super(botToken);
        this.botName = botName;
        this.handler = handler;
        System.out.println("🎭 [FAKE MODE] Инициализирован тестовый бот: " + botName);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callback = update.getCallbackQuery();
            System.out.println("🔄 [FAKE] Callback: " + callback.getData());
            handler.handleCallback(callback).ifPresent(this::sent);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String username = message.getFrom() != null
                    ? message.getFrom().getUserName()
                    : "unknown";
            System.out.println("💬 [FAKE] Сообщение от " + username + ": " + message.getText());
            handler.commands(message).ifPresent(this::sent);
        }
    }

    @Override
    public String getBotUsername() {

        return botName;
    }

    @Override
    public void sent(Content content) {
        System.out.println("📨 [FAKE] Отправка контента в чат #" + content.getChatId());

        if (content.getText() != null && !content.getText().isEmpty()) {
            System.out.println("📝 Текст: " + content.getText());
        }
        if (content.getPhoto() != null) {
            System.out.println("🖼️ Фото: " + getMediaName(content.getPhoto()));
        }
        if (content.getAudio() != null) {
            System.out.println("🎵 Аудио: " + getMediaName(content.getAudio()));
        }
        if (content.getVideo() != null) {
            System.out.println("🎬 Видео: " + getMediaName(content.getVideo()));
        }
        if (content.getMarkup() != null && content.getMarkup().getKeyboard() != null) {
            System.out.println("⌨️ Клавиатура: " + content.getMarkup().getKeyboard().size() + " кнопок");
        }
    }

    private String getMediaName(InputFile inputFile) {
        if (inputFile == null) {
            return "null";
        }
        String name = inputFile.getMediaName();
        return name != null ? name : "<без имени>";
    }

    public void receive(Content content) {
        Optional<Content> result = handler.receive(content);
        result.ifPresent(this::sent);
    }
}
