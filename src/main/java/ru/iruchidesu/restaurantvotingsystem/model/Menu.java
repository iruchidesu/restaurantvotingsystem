package ru.iruchidesu.restaurantvotingsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "day"}, name = "menu_restaurant_date_idx")})
public class Menu extends AbstractBaseEntity {

    @Column(name = "day", nullable = false, columnDefinition = "date default now()")
    @NotNull
    private LocalDate localDate = LocalDate.now();

    @Valid
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dish", joinColumns = @JoinColumn(name = "menu_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "name"}, name = "menu_dish_idx")})
    @JoinColumn(name = "menu_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Dish> dishes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JsonBackReference
    private Restaurant restaurant;

    public Menu() {

    }

    public Menu(Integer id, LocalDate localDate, List<Dish> dishes) {
        super(id);
        this.localDate = localDate;
        this.dishes = dishes;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "localDate=" + localDate +
                ", restaurant=" + restaurant +
                ", dishes=" + dishes +
                '}';
    }
}
