package ru.iruchidesu.restaurantvotingsystem.to;

import ru.iruchidesu.restaurantvotingsystem.model.Dish;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

public class MenuTo {
    @NotEmpty
    private List<Dish> dishes;

    public MenuTo() {
    }

    public MenuTo(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "dishes=" + dishes +
                '}';
    }
}
