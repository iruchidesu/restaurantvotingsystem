package ru.iruchidesu.restaurantvotingsystem.repository;

import ru.iruchidesu.restaurantvotingsystem.model.User;

public interface UserRepository {
    // null if not found, when updated
    User save(User user);

    // false if not found
    boolean delete(int id);

    // null if not found
    User get(int id);

    // null if not found
    User getByEmail(String email);
}
