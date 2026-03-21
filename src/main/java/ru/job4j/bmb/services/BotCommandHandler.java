package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class BotCommandHandler {

    @PostConstruct
    public void init() {
        System.out.println("BotCommandHandler initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("BotCommandHandler destroyed");
    }

    void receive(Content content) {
        System.out.println(content);
    }
}