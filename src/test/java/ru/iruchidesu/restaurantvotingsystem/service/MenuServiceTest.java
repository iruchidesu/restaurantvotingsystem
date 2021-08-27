package ru.iruchidesu.restaurantvotingsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.iruchidesu.restaurantvotingsystem.error.NotFoundException;
import ru.iruchidesu.restaurantvotingsystem.model.Dish;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.util.MenuUtil;
import ru.iruchidesu.restaurantvotingsystem.web.restaurant.RestaurantTestData;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.MenuTestData.MATCHER;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.MenuTestData.NOT_FOUND;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.MenuTestData.getNew;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.MenuTestData.getUpdated;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.MenuTestData.*;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.RestaurantTestData.*;

public class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    private MenuService service;

    @Test
    void create() {
        Menu created = service.create(MenuUtil.asTo(getNew()), RESTAURANT2_ID);
        int newId = created.id();
        Menu newMenu = getNew();
        newMenu.setId(newId);
        newMenu.setRestaurant(restaurant2);
        MATCHER.assertMatch(created, newMenu);
        MATCHER.assertMatch(service.get(newId), newMenu);
    }

    @Test
    void delete() {
        service.deleteTodayMenu(RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () -> service.get(MENU_TODAY_R1_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.deleteTodayMenu(RestaurantTestData.NOT_FOUND));
    }

    @Test
    void get() {
        Menu menu = service.get(MENU1_R1_ID);
        MATCHER.assertMatch(menu, menu1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void update() {
        Menu updated = getUpdated();
        updated.setRestaurant(restaurant1);
        service.update(MenuUtil.asTo(updated), RESTAURANT1_ID);
        MATCHER.assertMatch(service.get(MENU_TODAY_R1_ID), getUpdated());
    }

    @Test
    void updateNotFoundRestaurant() {
        Menu updated = getUpdated();
        updated.setRestaurant(null);
        validateRootCause(NotFoundException.class, () -> service.update(MenuUtil.asTo(updated), RestaurantTestData.NOT_FOUND));
    }

    @Test
    void updateWithEmptyDishes() {
        Menu updated = getUpdated();
        updated.setDishes(null);
        validateRootCause(ConstraintViolationException.class, () -> service.update(MenuUtil.asTo(updated), RESTAURANT1_ID));
    }

    @Test
    void getTodayMenu() {
        Menu menu = service.getTodayMenu(RESTAURANT1_ID);
        MATCHER.assertMatch(menu, menuTodayR1);
    }

    @Test
    void getNotFoundTodayMenu() {
        assertThrows(NotFoundException.class, () -> service.getTodayMenu(RESTAURANT2_ID));
    }

    @Test
    void getTodayMenuNotFoundRestaurant() {
        assertThrows(NotFoundException.class, () -> service.getTodayMenu(RestaurantTestData.NOT_FOUND));
    }

    @Test
    void getHistoryMenu() {
        List<Menu> all = service.getHistoryMenu(RESTAURANT1_ID);
        assertThat(all).isEqualTo(List.of(menuTodayR1, menu2, menu1));
    }

    @Test
    void getHistoryMenuNotFoundRestaurant() {
        List<Menu> all = service.getHistoryMenu(RestaurantTestData.NOT_FOUND);
        MATCHER.assertMatch(all, List.of());
    }

    @Test
    void createWithException() {
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(MenuUtil.asTo(new Menu(null, LocalDate.now(), null)), RESTAURANT1_ID));
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(MenuUtil.asTo(new Menu(null, LocalDate.now(), List.of(new Dish(" ", 840)))), RESTAURANT1_ID));
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(MenuUtil.asTo(new Menu(null, LocalDate.now(), List.of(new Dish("dish10", 0)))), RESTAURANT1_ID));
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(MenuUtil.asTo(new Menu(null, LocalDate.now(), List.of())), RESTAURANT1_ID));
        validateRootCause(NotFoundException.class,
                () -> service.create(MenuUtil.asTo(new Menu(null, LocalDate.now(), List.of(new Dish("dish10", 10)))), RestaurantTestData.NOT_FOUND));
    }
}
