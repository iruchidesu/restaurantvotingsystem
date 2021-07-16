package ru.iruchidesu.restaurantvotingsystem.to;

import ru.iruchidesu.restaurantvotingsystem.model.Dish;

import java.util.List;

public class MenuTo {
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
