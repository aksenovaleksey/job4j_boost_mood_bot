package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "award")
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer daysRequired;

    public Award() {
    }

    public Award(String name, String description, Integer daysRequired) {
        this.name = name;
        this.description = description;
        this.daysRequired = daysRequired;
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

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Integer getDaysRequired() {

        return daysRequired;
    }

    public void setDaysRequired(Integer daysRequired) {

        this.daysRequired = daysRequired;
    }
}
