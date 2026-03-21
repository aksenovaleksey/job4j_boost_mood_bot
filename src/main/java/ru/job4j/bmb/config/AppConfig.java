package ru.job4j.bmb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.url}")
    private String appUrl;

    @Value("${app.timeout}")
    private int timeout;

    // Геттеры для доступа к значениям
    public String getBotName() {
        return botName;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void printConfig() {
        System.out.println("Bot Name: " + botName);
        System.out.println("Bot Token: " + botToken);
        System.out.println("App Version: " + appVersion);
        System.out.println("App URL: " + appUrl);
        System.out.println("Timeout: " + timeout);
    }
}
