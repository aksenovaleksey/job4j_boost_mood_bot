package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.BeanNameAware;
import ru.job4j.bmb.content.Content;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Optional;

@Service
public class TelegramBotService implements BeanNameAware {
    private final BotCommandHandler handler;

    public TelegramBotService(BotCommandHandler handler) {
        this.handler = handler;
    }

    @PostConstruct
    public void init() {
        System.out.println("TelegramBotService initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("TelegramBotService destroyed");
    }

    public void receive(Content content) {
        Optional<Content> result = handler.receive(content);
        result.ifPresent(this::sendContent);
    }

    private void sendContent(Content content) {
        System.out.println("Sending content to chat: " + content.getChatId());
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Bean name: " + name);
    }
}