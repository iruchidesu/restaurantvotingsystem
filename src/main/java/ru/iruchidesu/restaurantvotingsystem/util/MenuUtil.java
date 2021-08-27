package ru.iruchidesu.restaurantvotingsystem.util;

import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.to.MenuTo;

public class MenuUtil {

    private MenuUtil() {
    }

    public static MenuTo asTo(Menu menu) {
        return new MenuTo(menu.getDishes());
    }
}
