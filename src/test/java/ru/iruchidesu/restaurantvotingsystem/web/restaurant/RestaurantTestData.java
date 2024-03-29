package ru.iruchidesu.restaurantvotingsystem.web.restaurant;

import ru.iruchidesu.restaurantvotingsystem.MatcherFactory;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.iruchidesu.restaurantvotingsystem.model.AbstractBaseEntity.START_SEQ;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.MenuTestData.menuTodayR1;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);
    public static final MatcherFactory.Matcher<Restaurant> MATCHER_WITH_MENU = MatcherFactory.usingAssertions(Restaurant.class,
            (a, e) -> {
                throw new UnsupportedOperationException();
            },
            (a, e) -> assertThat(a).isEqualTo(e));

    public static final int RESTAURANT1_ID = START_SEQ + 2;
    public static final int RESTAURANT2_ID = START_SEQ + 3;
    public static final int NOT_FOUND = 10;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "rest1", "address_rest1");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "rest2", "address_rest2");

    static {
        restaurant1.setMenus(List.of(menuTodayR1));
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "restNew", "address_restNew");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant();
        updated.setId(RESTAURANT1_ID);
        updated.setName("restUpdate");
        updated.setAddress("address_restUpdate");
        return updated;
    }

}
