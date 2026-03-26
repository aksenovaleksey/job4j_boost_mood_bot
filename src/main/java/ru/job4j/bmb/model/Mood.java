package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mood") // подставьте нужное имя таблицы
public class Mood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   }