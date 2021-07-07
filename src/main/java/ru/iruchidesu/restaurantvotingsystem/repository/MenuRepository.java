package ru.iruchidesu.restaurantvotingsystem.repository;

import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;

import java.util.List;

public interface MenuRepository {
    // null if not found, when updated
    Menu save(Menu menu, int userId);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Menu get(int id);

    Menu getTodayMenu(int restaurantId);

    List<Menu> getMenusByRestaurant(Restaurant restaurant);
}
