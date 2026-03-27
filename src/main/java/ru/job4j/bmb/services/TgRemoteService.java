package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.content.Content;

import java.util.Optional;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;
    private final TgUI tgUI;
    private final TgResponseHandler tgResponseHandler;
    private final BotCommandHandler botCommandHandler;

    public TgRemoteService(@Value("${telegram.bot.name}") String botName,
                           @Value("${telegram.bot.token}") String botToken,
                           TgUI tgUI,
                           TgResponseHandler tgResponseHandler,
                           BotCommandHandler botCommandHandler) {
        this.botName = botName;
        this.botToken = botToken;
        this.tgUI = tgUI;
        this.tgResponseHandler = tgResponseHandler;
        this.botCommandHandler = botCommandHandler;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public SendMessage sendButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Как настроение сегодня?");
        message.setReplyMarkup(tgUI.buildButtons());
        return message;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callback = update.getCallbackQuery();
            botCommandHandler.handleCallback(callback)
                    .ifPresent(this::sendContent);
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            botCommandHandler.commands(message)
                    .ifPresent(this::sendContent);
            return;
        }
    }

    private void sendContent(Content content) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(content.getChatId()));

        if (content.getText() != null && !content.getText().isEmpty()) {
            message.setText(content.getText());
        }

        if (content.getMarkup() != null) {
            message.setReplyMarkup(content.getMarkup());
        }

        send(message);
    }
}