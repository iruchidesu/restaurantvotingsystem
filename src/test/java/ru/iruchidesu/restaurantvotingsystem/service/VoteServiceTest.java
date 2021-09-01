package ru.iruchidesu.restaurantvotingsystem.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.iruchidesu.restaurantvotingsystem.error.NotFoundException;
import ru.iruchidesu.restaurantvotingsystem.error.VoteUpdateTimeException;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.web.restaurant.RestaurantTestData;
import ru.iruchidesu.restaurantvotingsystem.web.user.UserTestData;

import java.sql.BatchUpdateException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.RestaurantTestData.restaurant2;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.VoteTestData.MATCHER;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.VoteTestData.NOT_FOUND;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.VoteTestData.getNew;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.VoteTestData.getUpdated;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.VoteTestData.*;
import static ru.iruchidesu.restaurantvotingsystem.web.user.UserTestData.*;

public class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    private VoteService service;

    @Test
    void create() {
        Vote created = service.create(RESTAURANT2_ID, ADMIN_ID);
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);
        newVote.setRestaurant(restaurant2);
        newVote.setUser(admin);
        MATCHER.assertMatch(created, newVote);
        MATCHER.assertMatch(service.get(newId), newVote);
    }

    @Test
    void get() {
        Vote vote = service.get(VOTE_1_ID);
        MATCHER.assertMatch(vote, vote1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void update() {
        service.update(RESTAURANT2_ID, BEFORE_FORBIDDEN_TIME, USER_ID);
        MATCHER.assertMatch(service.get(VOTE_TODAY1_ID), getUpdated());
    }

    @Test
    void updateForbiddenTime() {
        VoteUpdateTimeException exception = assertThrows(VoteUpdateTimeException.class,
                () -> service.update(RESTAURANT2_ID, AFTER_FORBIDDEN_TIME, USER_ID));
        Assertions.assertEquals("it's too late to change your vote", exception.getMessage());
        MATCHER.assertMatch(service.get(VOTE_TODAY1_ID), voteToday1);
    }

    @Test
    void updateNotFoundRestaurant() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.update(RestaurantTestData.NOT_FOUND, BEFORE_FORBIDDEN_TIME, USER_ID));
        Assertions.assertEquals("Not found restaurant with id = 10", exception.getMessage());
        MATCHER.assertMatch(service.get(VOTE_TODAY1_ID), voteToday1);
    }

    @Test
    void updateNotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.update(RESTAURANT2_ID, BEFORE_FORBIDDEN_TIME, ADMIN_ID));
        Assertions.assertEquals("Not found vote with userId = 100001", exception.getMessage());
        MATCHER.assertMatch(service.get(VOTE_TODAY1_ID), voteToday1);
    }

    @Test
    void updateNotFoundUser() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.update(RESTAURANT2_ID, BEFORE_FORBIDDEN_TIME, UserTestData.NOT_FOUND));
        Assertions.assertEquals("Not found vote with userId = 10", exception.getMessage());
        MATCHER.assertMatch(service.get(VOTE_TODAY1_ID), voteToday1);
    }

    @Test
    void getAllVoteByUser() {
        List<Vote> allByUser = service.getAllVoteByUser(USER_ID);
        MATCHER.assertMatch(allByUser, voteToday1, vote1);
    }

    @Test
    void getTodayVoteByUser() {
        MATCHER.assertMatch(service.getVoteByUser(USER_ID, LocalDate.now()), voteToday1);
    }

    @Test
    void createWithException() {
        validateRootCause(NotFoundException.class,
                () -> service.create(RestaurantTestData.NOT_FOUND, ADMIN_ID));
        validateRootCause(BatchUpdateException.class,
                () -> service.create(RESTAURANT2_ID, UserTestData.NOT_FOUND));
    }
}
