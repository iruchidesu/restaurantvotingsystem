package ru.iruchidesu.restaurantvotingsystem.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.iruchidesu.restaurantvotingsystem.RestaurantTestData;
import ru.iruchidesu.restaurantvotingsystem.UserTestData;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.util.exception.NotFoundException;
import ru.iruchidesu.restaurantvotingsystem.util.exception.VoteUpdateTimeException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.RESTAURANT2_ID;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.restaurant2;
import static ru.iruchidesu.restaurantvotingsystem.UserTestData.*;
import static ru.iruchidesu.restaurantvotingsystem.VoteTestData.MATCHER;
import static ru.iruchidesu.restaurantvotingsystem.VoteTestData.NOT_FOUND;
import static ru.iruchidesu.restaurantvotingsystem.VoteTestData.getNew;
import static ru.iruchidesu.restaurantvotingsystem.VoteTestData.getUpdated;
import static ru.iruchidesu.restaurantvotingsystem.VoteTestData.*;

public class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    private VoteService service;

    @Test
    void create() {
        Vote created = service.create(getNew(), RESTAURANT2_ID, ADMIN_ID);
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);
        newVote.setRestaurant(restaurant2);
        newVote.setUser(admin);
        MATCHER.assertMatch(created, newVote);
        MATCHER.assertMatch(service.get(newId), newVote);
    }

    @Test
    void delete() {
        service.delete(VOTE_TODAY1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(VOTE_TODAY1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(VOTE_TODAY1_ID, ADMIN_ID));
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
    void getAll() {
        List<Vote> all = service.getAll();
        MATCHER.assertMatch(all, voteToday1, vote2, vote1);
    }

    @Test
    void update() {
        Vote updated = getUpdated();
        service.update(updated, BEFORE_FORBIDDEN_TIME, USER_ID);
        MATCHER.assertMatch(service.get(VOTE_TODAY1_ID), getUpdated());
    }

    @Test
    void updateForbiddenTime() {
        Vote updated = getUpdated();
        VoteUpdateTimeException exception = assertThrows(VoteUpdateTimeException.class, () -> service.update(updated, AFTER_FORBIDDEN_TIME, USER_ID));
        Assertions.assertEquals("it's too late to change your vote", exception.getMessage());
        MATCHER.assertMatch(service.get(VOTE_TODAY1_ID), voteToday1);
    }

    @Test
    void updateNotFoundRestaurant() {
        Vote updated = getUpdated();
        updated.setRestaurant(null);
        validateRootCause(ConstraintViolationException.class,
                () -> service.update(updated, BEFORE_FORBIDDEN_TIME, USER_ID));
    }

    @Test
    void updateNotOwn() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.update(getUpdated(), BEFORE_FORBIDDEN_TIME, ADMIN_ID));
        Assertions.assertEquals("Not found entity with id=" + VOTE_TODAY1_ID, exception.getMessage());
        MATCHER.assertMatch(service.get(VOTE_TODAY1_ID), voteToday1);
    }

    @Test
    void getAllVoteByUser() {
        List<Vote> allByUser = service.getAllVoteByUser(USER_ID);
        MATCHER.assertMatch(allByUser, voteToday1, vote1);
    }

    @Test
    void getTodayVoteByUser() {
        MATCHER.assertMatch(service.getTodayVoteByUser(USER_ID), voteToday1);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(new Vote(null, null), RESTAURANT2_ID, ADMIN_ID));
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(new Vote(null, LocalDate.now()), RestaurantTestData.NOT_FOUND, ADMIN_ID));
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(new Vote(null, LocalDate.now()), RESTAURANT2_ID, UserTestData.NOT_FOUND));
    }
}
