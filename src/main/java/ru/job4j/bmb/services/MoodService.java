package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class MoodService {

    @PostConstruct
    public void init() {
        System.out.println("MoodService initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("MoodService destroyed");
    }
}