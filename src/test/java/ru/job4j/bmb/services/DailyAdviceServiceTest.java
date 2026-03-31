package ru.job4j.bmb.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyAdviceServiceTest {

    @Mock
    private SentContent sentContent;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MoodLogRepository moodLogRepository;

    @InjectMocks
    private DailyAdviceService dailyAdviceService;

    @Test
    void whenGetDailyAdvice_thenReturnsContentWithAdvice() {
        User user = new User();
        user.setId(1L);
        user.setClientId(123L);
        user.setChatId(456L);
        user.setDailyAdviceEnabled(true);

        when(userRepository.findByChatIdAndClientId(456L, 123L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Content result = dailyAdviceService.getDailyAdvice(456L, 123L);

        assertThat(result).isNotNull();
        assertThat(result.getChatId()).isEqualTo(456L);
        assertThat(result.getText()).contains("Совет дня");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void whenToggleDailyAdviceEnable_thenUpdatesUserSetting() {
        User user = new User();
        user.setId(2L);
        user.setClientId(789L);
        user.setChatId(101L);
        user.setDailyAdviceEnabled(false);

        when(userRepository.findByChatIdAndClientId(101L, 789L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Content result = dailyAdviceService.toggleDailyAdvice(101L, 789L, true);

        assertThat(result.getText()).contains("включены");
        assertThat(user.isDailyAdviceEnabled()).isTrue();

        verify(userRepository).save(user);
    }

    @Test
    void whenToggleDailyAdviceDisable_thenUpdatesUserSetting() {
        User user = new User();
        user.setId(3L);
        user.setClientId(111L);
        user.setChatId(222L);
        user.setDailyAdviceEnabled(true);

        when(userRepository.findByChatIdAndClientId(222L, 111L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Content result = dailyAdviceService.toggleDailyAdvice(222L, 111L, false);

        assertThat(result.getText()).contains("выключены");
        assertThat(user.isDailyAdviceEnabled()).isFalse();
    }

    @Test
    void whenGetSettings_thenReturnsSettingsContent() {
        User user = new User();
        user.setId(4L);
        user.setClientId(333L);
        user.setChatId(444L);
        user.setDailyAdviceEnabled(true);

        when(userRepository.findByChatIdAndClientId(444L, 333L))
                .thenReturn(Optional.of(user));

        Content result = dailyAdviceService.getSettings(444L, 333L);

        assertThat(result).isNotNull();
        assertThat(result.getText()).contains("Настройки бота");
        assertThat(result.getText()).contains("✅ Включено");
    }

    @Test
    void whenCanSendAutoAdvice_withEnabledAndNoPreviousAdvice_thenReturnsTrue() {
        User user = new User();
        user.setDailyAdviceEnabled(true);
        user.setLastAdviceDate(null);

        boolean result = dailyAdviceService.canSendAutoAdvice(user);

        assertThat(result).isTrue();
    }

    @Test
    void whenCanSendAutoAdvice_withDisabled_thenReturnsFalse() {
        User user = new User();
        user.setDailyAdviceEnabled(false);
        user.setLastAdviceDate(Instant.now().getEpochSecond());

        boolean result = dailyAdviceService.canSendAutoAdvice(user);

        assertThat(result).isFalse();
    }

    @Test
    void whenCanSendAutoAdvice_withTodayAlreadySent_thenReturnsFalse() {
        User user = new User();
        user.setDailyAdviceEnabled(true);
        user.setLastAdviceDate(Instant.now().getEpochSecond());

        boolean result = dailyAdviceService.canSendAutoAdvice(user);

        assertThat(result).isFalse();
    }

    @Test
    void whenGenerateAdvice_withPositiveMood_thenReturnsPositiveAdvice() {
        User user = new User();
        user.setId(5L);
        user.setClientId(555L);
        user.setChatId(666L);

        Mood positiveMood = new Mood("Happy", true);
        MoodLog log = new MoodLog(user, positiveMood, Instant.now().getEpochSecond());

        when(userRepository.findByChatIdAndClientId(666L, 555L))
                .thenReturn(Optional.of(user));
        when(moodLogRepository.findByUser(user))
                .thenReturn(List.of(log));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Content result = dailyAdviceService.getDailyAdvice(666L, 555L);

        assertThat(result.getText()).contains("Совет дня");
    }

    @Test
    void whenGenerateAdvice_withNegativeMood_thenReturnsSupportiveAdvice() {
        User user = new User();
        user.setId(6L);
        user.setClientId(777L);
        user.setChatId(888L);

        Mood negativeMood = new Mood("Sad", false);
        MoodLog log = new MoodLog(user, negativeMood, Instant.now().getEpochSecond());

        when(userRepository.findByChatIdAndClientId(888L, 777L))
                .thenReturn(Optional.of(user));
        when(moodLogRepository.findByUser(user))
                .thenReturn(List.of(log));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Content result = dailyAdviceService.getDailyAdvice(888L, 777L);

        assertThat(result.getText()).contains("Совет дня");
    }
}