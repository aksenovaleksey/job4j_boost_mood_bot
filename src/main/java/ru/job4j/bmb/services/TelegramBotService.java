package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.config.condition.RealModeCondition;
import ru.job4j.bmb.content.Content;

import java.util.Optional;

@Service
@Conditional(RealModeCondition.class)
public class TelegramBotService extends TelegramLongPollingBot implements SentContent {

    private final BotCommandHandler handler;
    private final String botName;

    public TelegramBotService(
            @Value("${telegram.bot.name}") String botName,
            @Value("${telegram.bot.token}") String botToken,
            BotCommandHandler handler) {
        super(botToken);
        this.botName = botName;
        this.handler = handler;
        System.out.println("✅ [REAL MODE] Инициализирован реальный бот: " + botName);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callback = update.getCallbackQuery();
            handler.handleCallback(callback).ifPresent(this::sent);
        } else if (update.hasMessage()
                && update.getMessage().hasText()) {
            Message message = update.getMessage();
            handler.commands(message).ifPresent(this::sent);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void sent(Content content) {
        try {
            if (content.getAudio() != null) {
                sendAudio(content);
                return;
            }
            if (content.getPhoto() != null) {
                sendPhoto(content);
                return;
            }
            if (content.getVideo() != null) {
                sendVideo(content);
                return;
            }
            if (content.getText() != null && !content.getText().isEmpty()) {
                sendText(content);
            }
        } catch (TelegramApiException e) {
            throw new SentContentException(
                    String.format("Failed to send content to chat %d", content.getChatId()),
                    e
            );
        }
    }

    private void sendAudio(Content content) throws TelegramApiException {
        SendAudio sendAudio = new SendAudio();
        sendAudio.setChatId(String.valueOf(content.getChatId()));
        sendAudio.setAudio(content.getAudio());
        if (content.getText() != null && !content.getText().isEmpty()) {
            sendAudio.setCaption(content.getText());
        }
        execute(sendAudio);
    }

    private void sendPhoto(Content content) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(content.getChatId()));
        sendPhoto.setPhoto(content.getPhoto());
        if (content.getText() != null && !content.getText().isEmpty()) {
            sendPhoto.setCaption(content.getText());
        }
        execute(sendPhoto);
    }

    private void sendVideo(Content content) throws TelegramApiException {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(String.valueOf(content.getChatId()));
        sendVideo.setVideo(content.getVideo());
        if (content.getText() != null && !content.getText().isEmpty()) {
            sendVideo.setCaption(content.getText());
        }
        execute(sendVideo);
    }

    private void sendText(Content content) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(content.getChatId()));
        sendMessage.setText(content.getText());
        if (content.getMarkup() != null) {
            sendMessage.setReplyMarkup(content.getMarkup());
        }
        execute(sendMessage);
    }

    public void receive(Content content) {
        Optional<Content> result = handler.receive(content);
        result.ifPresent(this::sent);
    }
}
