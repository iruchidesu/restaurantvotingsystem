package ru.iruchidesu.restaurantvotingsystem.util;

import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.to.MenuTo;
import ru.iruchidesu.restaurantvotingsystem.to.UserTo;

import java.util.ArrayList;
import java.util.List;

public class ToUtil {
    public static MenuTo getMenuTo(Menu menu) {
        return new MenuTo(menu.getDishes());
    }

    public static UserTo getUserTo(User user) {
        return new UserTo(user.id(), user.getName(), user.getEmail(), user.getPassword());
    }

    public static List<UserTo> getUserTos(List<User> users) {
        List<UserTo> tos = new ArrayList<>();
        users.forEach(user -> tos.add(getUserTo(user)));
        return tos;
    }
}
