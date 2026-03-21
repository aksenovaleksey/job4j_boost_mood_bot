package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class AchievementService {

    @PostConstruct
    public void init() {
        System.out.println("AchievementService initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("AchievementService destroyed");
    }
}