package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.BeanNameAware;
import ru.job4j.bmb.content.Content;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class BotCommandHandler implements BeanNameAware {

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

    @Override
    public void setBeanName(String name) {
        System.out.println("Bean name: " + name);
    }
}