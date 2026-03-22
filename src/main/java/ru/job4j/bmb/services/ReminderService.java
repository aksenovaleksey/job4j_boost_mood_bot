package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.BeanNameAware;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class ReminderService implements BeanNameAware {

    @PostConstruct
    public void init() {
        System.out.println("ReminderService initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("ReminderService destroyed");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Bean name: " + name);
    }
}
