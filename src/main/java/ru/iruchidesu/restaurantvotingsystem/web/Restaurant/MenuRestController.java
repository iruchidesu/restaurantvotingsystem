package ru.iruchidesu.restaurantvotingsystem.web.Restaurant;

import org.springframework.stereotype.Controller;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.service.MenuService;
import ru.iruchidesu.restaurantvotingsystem.to.MenuTo;
import ru.iruchidesu.restaurantvotingsystem.util.ToUtil;

import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNew;

@Controller
public class MenuRestController {

    private final MenuService service;

    public MenuRestController(MenuService service) {
        this.service = service;
    }

    public Menu create(Menu menu, int restaurantId) {
        checkNew(menu);
        return service.create(menu, restaurantId);
    }

    public void update(Menu menu, int id) {
        assureIdConsistent(menu, id);
        //TODO MenuRestController authorized userID
        service.update(menu);
    }

    public MenuTo getTodayMenu(int restaurantId) {
        return ToUtil.getMenuTo(service.getTodayMenu(restaurantId));
    }

    public List<Menu> getHistoryMenu(int restaurantId) {
        return service.getHistoryMenu(restaurantId);
    }

    public void delete(int id) {
        //TODO MenuRestController authorized userID
        service.delete(id, 100000);
    }
}
