package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "moodcontent")
public class MoodContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    @Column(columnDefinition = "TEXT")
    private String content;

    public MoodContent() {}

    public MoodContent(Mood mood, String content) {
        this.mood = mood;
        this.content = content;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Mood getMood() {
        return mood;
    }
    public void setMood(Mood mood) {
        this.mood = mood;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}