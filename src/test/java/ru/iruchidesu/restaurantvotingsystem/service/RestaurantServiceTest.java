package ru.iruchidesu.restaurantvotingsystem.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.MATCHER;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.getNew;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.getUpdated;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.*;
import static ru.iruchidesu.restaurantvotingsystem.UserTestData.NOT_FOUND;
import static ru.iruchidesu.restaurantvotingsystem.UserTestData.*;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    public void create() {
        Restaurant created = service.create(getNew(), ADMIN_ID);
        int newId = created.id();
        Restaurant newRestaurant = getNew();
        newRestaurant.setId(newId);
        MATCHER.assertMatch(created, newRestaurant);
        MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    public void createByUser() {
        Restaurant created = service.create(getNew(), USER_ID);

    }

    @Test
    public void delete() {
        service.delete(RESTAURANT1_ID, ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(RESTAURANT1_ID));
    }

    @Test
    public void deleteByUser() {
        service.delete(RESTAURANT1_ID, USER_ID);
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void get() {
        Restaurant restaurant = service.get(RESTAURANT2_ID);
        MATCHER.assertMatch(restaurant, restaurant2);
    }

    @Test
    public void getByName() {
        Restaurant restaurant = service.getByName("rest1");
        MATCHER.assertMatch(restaurant, restaurant1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void getAll() {
        List<Restaurant> all = service.getAll();
        MATCHER.assertMatch(all, restaurant1, restaurant2);
    }

    @Test
    public void update() {
        Restaurant updated = getUpdated();
        service.update(updated, ADMIN_ID);
        MATCHER.assertMatch(service.get(RESTAURANT1_ID), getUpdated());
    }

    @Test
    public void updateByUser() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.update(getUpdated(), USER_ID));
        Assert.assertEquals("Not found entity with id=" + RESTAURANT1_ID, exception.getMessage());
        MATCHER.assertMatch(service.get(RESTAURANT1_ID), restaurant1);
    }

    @Test
    public void createWithException() throws Exception {
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Restaurant(null, "  ", "addressRest4"), ADMIN_ID));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Restaurant(null, "rest5", "  "), ADMIN_ID));
    }
}