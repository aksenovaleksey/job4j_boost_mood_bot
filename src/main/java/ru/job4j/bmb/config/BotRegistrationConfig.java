package ru.job4j.bmb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.job4j.bmb.services.SentContent;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

@Configuration
public class BotRegistrationConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(SentContent bot) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            if (bot instanceof TelegramLongPollingBot longPollingBot) {
                botsApi.registerBot(longPollingBot);
            }
            return botsApi;
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to register Telegram bot", e);
        }
    }
}
