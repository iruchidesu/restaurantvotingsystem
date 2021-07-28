package ru.iruchidesu.restaurantvotingsystem;

import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;

import static ru.iruchidesu.restaurantvotingsystem.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final MatcherFactory<Restaurant> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    public static final int RESTAURANT1_ID = START_SEQ + 2;
    public static final int RESTAURANT2_ID = START_SEQ + 3;
    public static final int NOT_FOUND = 10;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "rest1", "address_rest1");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "rest2", "address_rest2");

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
