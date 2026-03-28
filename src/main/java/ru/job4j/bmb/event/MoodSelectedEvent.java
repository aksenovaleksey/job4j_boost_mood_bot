package ru.job4j.bmb.event;

import org.springframework.context.ApplicationEvent;
import ru.job4j.bmb.model.User;

public class MoodSelectedEvent extends ApplicationEvent {
    private final User user;
    private final Long moodId;

    public MoodSelectedEvent(Object source, User user, Long moodId) {
        super(source);
        this.user = user;
        this.moodId = moodId;
    }

    public User getUser() {
        return user;
    }

    public Long getMoodId() {
        return moodId;
    }
}