package ru.iruchidesu.restaurantvotingsystem.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.iruchidesu.restaurantvotingsystem.RestaurantTestData;
import ru.iruchidesu.restaurantvotingsystem.model.Dish;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static ru.iruchidesu.restaurantvotingsystem.MenuTestData.MATCHER;
import static ru.iruchidesu.restaurantvotingsystem.MenuTestData.NOT_FOUND;
import static ru.iruchidesu.restaurantvotingsystem.MenuTestData.getNew;
import static ru.iruchidesu.restaurantvotingsystem.MenuTestData.getUpdated;
import static ru.iruchidesu.restaurantvotingsystem.MenuTestData.*;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.*;
import static ru.iruchidesu.restaurantvotingsystem.UserTestData.ADMIN_ID;
import static ru.iruchidesu.restaurantvotingsystem.UserTestData.USER_ID;

public class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    private MenuService service;

    @Test
    public void create() {
        Menu created = service.create(getNew(), RESTAURANT2_ID);
        int newId = created.id();
        Menu newMenu = getNew();
        newMenu.setId(newId);
        newMenu.setRestaurant(restaurant2);
        MATCHER.assertMatch(created, newMenu);
        MATCHER.assertMatch(service.get(newId), newMenu);
    }

    @Test
    public void createWithNotNowDate() {
        Menu newMenu = getNew();
        newMenu.setLocalDate(LocalDate.now().minusDays(1));
        Menu created = service.create(newMenu, RESTAURANT2_ID);
        assertNull(created);
    }

    @Test
    public void createByUser() {
        Menu created = service.create(getNew(), RESTAURANT2_ID);
        assertNull(created);
    }

    @Test
    public void delete() {
        service.delete(MENU_TODAY_R1_ID, ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(MENU_TODAY_R1_ID));
    }

    @Test
    public void deleteByUser() {
        assertThrows(NotFoundException.class, () -> service.delete(MENU_TODAY_R1_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void get() {
        Menu menu = service.get(MENU1_R1_ID);
        MATCHER.assertMatch(menu, menu1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void update() {
        Menu updated = getUpdated();
        updated.setRestaurant(restaurant1);
        service.update(updated);
        MATCHER.assertMatch(service.get(MENU_TODAY_R1_ID), getUpdated());
    }

    @Test
    public void updateNotFoundRestaurant() {
        Menu updated = getUpdated();
        updated.setRestaurant(null);
        validateRootCause(ConstraintViolationException.class, () -> service.update(updated));
    }

    @Test
    public void updateWithEmptyDishes() {
        Menu updated = getUpdated();
        updated.setDishes(List.of());
        validateRootCause(ConstraintViolationException.class, () -> service.update(updated));
    }

    @Test
    public void updateByUser() {
        Menu updated = getUpdated();
        updated.setRestaurant(restaurant1);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.update(updated));
        Assert.assertEquals("Not found entity with id=" + MENU_TODAY_R1_ID, exception.getMessage());
        MATCHER.assertMatch(service.get(MENU_TODAY_R1_ID), menuTodayR1);
    }

    @Test
    public void getTodayMenu() {
        Menu menu = service.getTodayMenu(RESTAURANT1_ID);
        MATCHER.assertMatch(menu, menuTodayR1);
    }

    @Test
    public void getNotFoundTodayMenu() {
        assertThrows(NotFoundException.class, () -> service.getTodayMenu(RESTAURANT2_ID));
    }

    @Test
    public void getTodayMenuNotFoundRestaurant() {
        assertThrows(NotFoundException.class, () -> service.getTodayMenu(RestaurantTestData.NOT_FOUND));
    }

    @Test
    public void getHistoryMenu() {
        List<Menu> all = service.getHistoryMenu(RESTAURANT1_ID);
        MATCHER.assertMatchWithoutIgnore(all, List.of(menuTodayR1, menu2, menu1));
    }

    @Test
    public void getHistoryMenuNotFoundRestaurant() {
        List<Menu> all = service.getHistoryMenu(RestaurantTestData.NOT_FOUND);
        MATCHER.assertMatch(all, List.of());
    }

    @Test
    public void createWithException() throws Exception {
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(new Menu(null, LocalDate.now(), null), RESTAURANT1_ID));
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(new Menu(null, LocalDate.now(), List.of(new Dish(" ", 840))), RESTAURANT1_ID));
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(new Menu(null, LocalDate.now(), List.of(new Dish("dish10", 0))), RESTAURANT1_ID));
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(new Menu(null, LocalDate.now(), List.of()), RESTAURANT1_ID));
        validateRootCause(ConstraintViolationException.class,
                () -> service.create(new Menu(null, LocalDate.now(), List.of(new Dish("dish10", 10))), RestaurantTestData.NOT_FOUND));
    }
}
