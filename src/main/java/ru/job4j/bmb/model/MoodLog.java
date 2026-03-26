package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "moodlog") // подставьте нужное имя таблицы
public class MoodLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
