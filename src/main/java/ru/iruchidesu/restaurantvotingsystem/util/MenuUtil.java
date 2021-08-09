package ru.iruchidesu.restaurantvotingsystem.util;

import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.to.MenuTo;

import java.util.ArrayList;
import java.util.List;

public class MenuUtil {
    public static MenuTo asTo(Menu menu) {
        return new MenuTo(menu.getDishes());
    }

    public static List<MenuTo> asListTo(List<Menu> menus) {
        List<MenuTo> tos = new ArrayList<>();
        menus.forEach(menu -> tos.add(asTo(menu)));
        return tos;
    }
}
