package ru.iruchidesu.restaurantvotingsystem;

import ru.iruchidesu.restaurantvotingsystem.model.Dish;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.restaurant1;
import static ru.iruchidesu.restaurantvotingsystem.model.AbstractBaseEntity.START_SEQ;

public class MenuTestData {
    public static final MatcherFactory<Menu> MATCHER = MatcherFactory.usingIgnoringFieldsComparator("restaurant");

    public static final int MENU1_R1_ID = START_SEQ + 4;
    public static final int MENU2_R1_ID = START_SEQ + 5;
    public static final int MENU_TODAY_R1_ID = START_SEQ + 6;
    public static final int NOT_FOUND = 10;

    public static final List<Dish> dishesMenu1 = List.of(new Dish("menu1_dish1", 15600), new Dish("menu1_dish2", 15700),
            new Dish("menu1_dish3", 6500), new Dish("menu1_dish4", 25700), new Dish("menu1_dish5", 35050));
    public static final List<Dish> dishesMenu2 = List.of(new Dish("menu2_dish1", 87000), new Dish("menu2_dish2", 31500),
            new Dish("menu2_dish3", 1500), new Dish("menu2_dish4", 104000), new Dish("menu2_dish5", 28700));
    public static final List<Dish> dishesMenuTodayR1 = List.of(new Dish("menu3_dish1", 31500), new Dish("menu3_dish2", 14400),
            new Dish("menu3_dish3", 40000));
    public static final List<Dish> dishesMenuTodayNew = List.of(new Dish("menuNew_dish1", 2160), new Dish("menuNew_dish2", 17400),
            new Dish("menuNew_dish3", 7000));

    public static final Menu menu1 = new Menu(MENU1_R1_ID, LocalDate.of(2021, Month.JULY, 17), dishesMenu1);
    public static final Menu menu2 = new Menu(MENU2_R1_ID, LocalDate.of(2021, Month.JULY, 18), dishesMenu2);
    public static final Menu menuTodayR1 = new Menu(MENU_TODAY_R1_ID, LocalDate.now(), dishesMenuTodayR1);

    static {
        menu1.setRestaurant(restaurant1);
        menu1.setLocalDate(LocalDate.of(2021, Month.JULY, 17));
        menu2.setRestaurant(restaurant1);
        menu2.setLocalDate(LocalDate.of(2021, Month.JULY, 18));
        menuTodayR1.setRestaurant(restaurant1);
    }

    public static Menu getNew() {
        return new Menu(null, LocalDate.now(), dishesMenuTodayNew);
    }

    public static Menu getUpdated() {
        List<Dish> updateDishes = List.of(new Dish("MenuUpdate_dish1", 84000), new Dish("MenuUpdate_dish2", 63500));
        return new Menu(MENU_TODAY_R1_ID, LocalDate.now(), updateDishes);
    }
}
