package ru.job4j.bmb;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class RecommendationEngine {

    @PostConstruct
    public void init() {
        System.out.println("RecommendationEngine initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("RecommendationEngine destroyed");
    }
}