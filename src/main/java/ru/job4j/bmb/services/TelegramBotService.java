package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.BeanNameAware;
import ru.job4j.bmb.content.Content;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

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
        handler.receive(content);
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Bean name: " + name);
    }
}