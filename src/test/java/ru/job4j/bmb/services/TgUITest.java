package ru.job4j.bmb.services;  // ← Исправлено с ru.job4j.bmb.component

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.job4j.bmb.repositories.MoodFakeRepository;  // ← Проверьте импорт
import ru.job4j.bmb.repository.MoodRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {TgUI.class, MoodFakeRepository.class})
class TgUITest {

    @Autowired
    @Qualifier("moodFakeRepository")  // ← Должно совпадать с @Component("...")
    private MoodRepository moodRepository;

    @Test
    void whenBtnGood() {
        assertThat(moodRepository).isNotNull();
    }
}
