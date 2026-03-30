package ru.job4j.bmb.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.job4j.bmb.repositories.MoodFakeRepository;
import ru.job4j.bmb.repository.MoodRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TgUITest {

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public MoodRepository moodRepository() {
            return new MoodFakeRepository();
        }

        @Bean
        public TgUI tgUI(MoodRepository moodRepository) {
            return new TgUI(moodRepository);
        }
    }

    @Autowired
    private TgUI tgUI;

    @Test
    void whenBtnGood() {
        assertThat(tgUI).isNotNull();
    }
}