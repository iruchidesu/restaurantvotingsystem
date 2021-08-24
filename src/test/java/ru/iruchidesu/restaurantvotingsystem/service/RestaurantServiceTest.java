package ru.iruchidesu.restaurantvotingsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.iruchidesu.restaurantvotingsystem.error.NotFoundException;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.*;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    void create() {
        Restaurant created = service.create(getNew());
        int newId = created.id();
        Restaurant newRestaurant = getNew();
        newRestaurant.setId(newId);
        MATCHER.assertMatch(created, newRestaurant);
        MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    void delete() {
        service.delete(RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () -> service.get(RESTAURANT1_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Restaurant restaurant = service.get(RESTAURANT2_ID);
        MATCHER.assertMatch(restaurant, restaurant2);
    }

    @Test
    void getByName() {
        Restaurant restaurant = service.getByName("rest1");
        MATCHER.assertMatch(restaurant, restaurant1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void getAll() {
        List<Restaurant> all = service.getAll();
        MATCHER.assertMatch(all, restaurant1, restaurant2);
    }

    @Test
    void update() {
        Restaurant updated = getUpdated();
        service.update(updated);
        MATCHER.assertMatch(service.get(RESTAURANT1_ID), getUpdated());
    }

    @Test
    void getWithMenu() {
        List<Restaurant> restaurant = service.getWithMenu();
        MATCHER_WITH_MENU.assertMatch(restaurant, List.of(restaurant1));
    }

    @Test
    void createWithException() {
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Restaurant(null, "  ", "addressRest4")));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Restaurant(null, "rest5", "  ")));
    }
}
