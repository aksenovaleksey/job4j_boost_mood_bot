package ru.job4j.bmb.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.ContentProviderAudio;
import ru.job4j.bmb.content.ContentProviderImage;
import ru.job4j.bmb.content.ContentProviderText;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {
        RecommendationEngine.class,
        ContentProviderAudio.class,
        ContentProviderImage.class,
        ContentProviderText.class
})
class RecommendationEngineTest {

    @Autowired
    private RecommendationEngine recommendationEngine;

    @Test
    void whenRecommendFor_thenReturnsContentWithChatId() {
        Long chatId = 12345L;
        Long moodId = 1L;

        Content result = recommendationEngine.recommendFor(chatId, moodId);

        assertThat(result).isNotNull();
        assertThat(result.getChatId()).isEqualTo(chatId);
    }

    @Test
    void whenRecommendByTypeText_thenReturnsTextContent() {
        Long chatId = 67890L;
        Long moodId = 3L;

        Content result = recommendationEngine.recommendByType(
                chatId, moodId, ContentProviderText.class);

        assertThat(result).isNotNull();
        assertThat(result.getChatId()).isEqualTo(chatId);
        assertThat(result.getText()).isNotNull();
        assertThat(result.getText()).isNotEmpty();
    }

    @Test
    void whenRecommendByTypeImage_thenReturnsImageContent() {
        Long chatId = 11111L;
        Long moodId = 2L;

        Content result = recommendationEngine.recommendByType(
                chatId, moodId, ContentProviderImage.class);

        assertThat(result).isNotNull();
        assertThat(result.getChatId()).isEqualTo(chatId);
        assertThat(result.getPhoto()).isNotNull();
    }

    @Test
    void whenRecommendByTypeAudio_thenReturnsAudioContent() {
        Long chatId = 22222L;
        Long moodId = 4L;

        Content result = recommendationEngine.recommendByType(
                chatId, moodId, ContentProviderAudio.class);

        assertThat(result).isNotNull();
        assertThat(result.getChatId()).isEqualTo(chatId);
        assertThat(result.getAudio()).isNotNull();
    }

    @Test
    void whenMoodIdNotFound_thenReturnsDefaultContent() {
        Long chatId = 99999L;
        Long unknownMoodId = 999L;

        Content result = recommendationEngine.recommendByType(
                chatId, unknownMoodId, ContentProviderText.class);

        assertThat(result).isNotNull();
        assertThat(result.getText()).contains("Держись");
    }
}