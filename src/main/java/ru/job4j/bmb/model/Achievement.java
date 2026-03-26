package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "achievement") // подставьте нужное имя таблицы
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
