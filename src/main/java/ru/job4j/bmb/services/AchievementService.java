package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.BeanNameAware;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class AchievementService implements BeanNameAware {

    @PostConstruct
    public void init() {
        System.out.println("AchievementService initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("AchievementService destroyed");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Bean name: " + name);
    }
}