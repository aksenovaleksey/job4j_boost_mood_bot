package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "moodcontent") // подставьте нужное имя таблицы
public class MoodContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
