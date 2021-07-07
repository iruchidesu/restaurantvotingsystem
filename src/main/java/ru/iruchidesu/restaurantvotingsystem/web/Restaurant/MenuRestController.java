package ru.iruchidesu.restaurantvotingsystem.web.Restaurant;

import org.springframework.stereotype.Controller;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.service.MenuService;

import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNew;

@Controller
public class MenuRestController {

    private final MenuService service;

    public MenuRestController(MenuService service) {
        this.service = service;
    }

    public Menu create(Menu menu) {
        checkNew(menu);
        //TODO MenuRestController userID
        return service.create(menu, 100001);
    }

    public void update(Menu menu, int id) {
        assureIdConsistent(menu, id);
        //TODO MenuRestController userID
        service.update(menu, 100001);
    }

    public Menu getTodayMenu(int restaurantId) {
        return service.getTodayMenu(restaurantId);
    }

    public List<Menu> getHistoryMenu(Restaurant restaurant) {
        return service.getHistoryMenu(restaurant);
    }

    public void delete(int id) {
        //TODO MenuRestController userID
        service.delete(id, 100000);
    }
}
