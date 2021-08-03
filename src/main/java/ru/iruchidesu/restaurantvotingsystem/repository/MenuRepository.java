package ru.iruchidesu.restaurantvotingsystem.repository;

import ru.iruchidesu.restaurantvotingsystem.model.Menu;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository {
    // null if not found, when updated
    Menu save(Menu menu);

    // false if not found
    boolean deleteByDate(int restaurantId, LocalDate localDate);

    // null if not found
    Menu get(int id);

    Menu getMenuByDate(int restaurantId, LocalDate localDate);

    List<Menu> getMenusByRestaurant(int restaurantId);
}
