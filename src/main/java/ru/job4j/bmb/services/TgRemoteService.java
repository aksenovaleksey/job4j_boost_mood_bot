package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;
    private final TgUI tgUI;
    private final TgResponseHandler tgResponseHandler;

    public TgRemoteService(@Value("${telegram.bot.name}") String botName,
                           @Value("${telegram.bot.token}") String botToken,
                           TgUI tgUI,
                           TgResponseHandler tgResponseHandler) {
        this.botName = botName;
        this.botToken = botToken;
        this.tgUI = tgUI;
        this.tgResponseHandler = tgResponseHandler;
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
            var callbackQuery = update.getCallbackQuery();
            var data = callbackQuery.getData();
            var chatId = callbackQuery.getMessage().getChatId();

            Long moodId = null;
            try {
                moodId = Long.valueOf(data);
            } catch (NumberFormatException e) {
                moodId = -1L;
            }

            SendMessage response = new SendMessage();
            response.setChatId(String.valueOf(chatId));
            response.setText(tgResponseHandler.getResponse(moodId));
            send(response);
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            send(sendButtons(chatId));
        }
    }
}
