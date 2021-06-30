package ru.iruchidesu.restaurantvotingsystem.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "voting_date"}, name = "user_voting_date_idx")})
public class Vote extends AbstractBaseEntity {
    @Column(name = "voting_date", nullable = false, columnDefinition = "date default CURRENT_DATE")
    @NotNull
    private LocalDate votingDate = LocalDate.now();

    @Column(name = "voting_time", nullable = false, columnDefinition = "time default CURRENT_TIME")
    @NotNull
    private LocalTime votingTime = LocalTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    public Vote() {

    }

    public Vote(Integer id, LocalDate votingDate, LocalTime votingTime) {
        super(id);
        this.votingDate = votingDate;
        this.votingTime = votingTime;
    }

    public LocalDate getVotingDate() {
        return votingDate;
    }

    public void setVotingDate(LocalDate votingDate) {
        this.votingDate = votingDate;
    }

    public LocalTime getVotingTime() {
        return votingTime;
    }

    public void setVotingTime(LocalTime votingTime) {
        this.votingTime = votingTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
