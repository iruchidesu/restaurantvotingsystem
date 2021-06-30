package ru.iruchidesu.restaurantvotingsystem.web.Restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.service.RestaurantService;

import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNew;

@Controller
public class RestaurantRestController {

    @Autowired
    private RestaurantService service;

    public Restaurant create(Restaurant restaurant) {
        checkNew(restaurant);
        //TODO RestaurantRestController userID create
        return service.create(restaurant, 100001);
    }

    public void update(Restaurant restaurant, int id) {
        assureIdConsistent(restaurant, id);
        //TODO RestaurantRestController userID update
        service.update(restaurant, 100001);
    }

    public Restaurant get(int id) {
        return service.get(id);
    }

    public List<Restaurant> getAll() {
        return service.getAll();
    }

    public Restaurant getByName(String name) {
        return service.getByName(name);
    }

    public void delete(int id) {
        //TODO RestaurantRestController userID delete
        service.delete(id, 100001);
    }
}
