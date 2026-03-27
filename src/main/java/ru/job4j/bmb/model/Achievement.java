package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "achievement")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "award_id")
    private Award award;

    @Column(name = "awarded_at")
    private Long awardedAt;

    public Achievement() {}

    public Achievement(User user, Award award, Long awardedAt) {
        this.user = user;
        this.award = award;
        this.awardedAt = awardedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Award getAward() { return award; }
    public void setAward(Award award) { this.award = award; }

    public Long getAwardedAt() { return awardedAt; }
    public void setAwardedAt(Long awardedAt) { this.awardedAt = awardedAt; }

    public String getName() {
        return award != null ? award.getName() : "Unknown";
    }
}
