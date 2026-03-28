package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mood")
public class Mood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean isPositive;

    public Mood() {}

    public Mood(String name, Boolean isPositive) {
        this.name = name;
        this.isPositive = isPositive;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Boolean getIsPositive() {
        return isPositive;
    }
    public void setIsPositive(Boolean positive) {
        isPositive = positive;
    }
}

