package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "award") // подставьте нужное имя таблицы
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
