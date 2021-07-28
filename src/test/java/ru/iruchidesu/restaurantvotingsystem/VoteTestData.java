package ru.iruchidesu.restaurantvotingsystem;

import ru.iruchidesu.restaurantvotingsystem.model.Vote;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.restaurant1;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.restaurant2;
import static ru.iruchidesu.restaurantvotingsystem.UserTestData.admin;
import static ru.iruchidesu.restaurantvotingsystem.UserTestData.user;
import static ru.iruchidesu.restaurantvotingsystem.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static final MatcherFactory<Vote> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user");

    public static final int VOTE_1_ID = START_SEQ + 7;
    public static final int VOTE_2_ID = START_SEQ + 8;
    public static final int VOTE_TODAY1_ID = START_SEQ + 9;
    public static final int NOT_FOUND = 10;

    public static final LocalTime BEFORE_FORBIDDEN_TIME = LocalTime.of(10, 40);
    public static final LocalTime AFTER_FORBIDDEN_TIME = LocalTime.of(12, 3);

    public static final Vote vote1 = new Vote(VOTE_1_ID, LocalDate.of(2021, Month.JULY, 17));
    public static final Vote vote2 = new Vote(VOTE_2_ID, LocalDate.of(2021, Month.JULY, 18));
    public static final Vote voteToday1 = new Vote(VOTE_TODAY1_ID, LocalDate.now());

    public static final List<Vote> all = List.of(voteToday1, vote2, vote1);

    static {
        vote1.setRestaurant(restaurant1);
        vote1.setUser(user);
        vote2.setRestaurant(restaurant1);
        vote2.setUser(admin);
        voteToday1.setRestaurant(restaurant1);
        voteToday1.setUser(user);
    }

    public static Vote getNew() {
        return new Vote(null, LocalDate.now());
    }

    public static Vote getUpdated() {
        Vote updated = new Vote();
        updated.setId(VOTE_TODAY1_ID);
        updated.setRestaurant(restaurant2);
        updated.setUser(user);
        return updated;
    }
}
