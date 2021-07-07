package ru.iruchidesu.restaurantvotingsystem.repository;

import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {
    // null if not found, when updated
    Restaurant save(Restaurant restaurant, int userId);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Restaurant get(int id);

    // null if not found
    Restaurant getByName(String name);

    List<Restaurant> getAll();
}
