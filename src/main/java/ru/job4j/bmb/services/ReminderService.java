package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class ReminderService {

    @PostConstruct
    public void init() {
        System.out.println("ReminderService initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("ReminderService destroyed");
    }
}
