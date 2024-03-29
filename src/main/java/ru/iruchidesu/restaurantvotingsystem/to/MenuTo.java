package ru.iruchidesu.restaurantvotingsystem.to;

import ru.iruchidesu.restaurantvotingsystem.model.Dish;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

public class MenuTo {
    @NotEmpty
    @Valid
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuTo menuTo = (MenuTo) o;
        return dishes.equals(menuTo.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishes);
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "dishes=" + dishes +
                '}';
    }
}
