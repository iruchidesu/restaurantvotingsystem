package ru.iruchidesu.restaurantvotingsystem.web.Restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.service.MenuService;

import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNew;

@Controller
public class MenuRestController {
    @Autowired
    private MenuService service;

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

    public Menu get(int id) {
        return service.get(id);
    }

    public List<Menu> getAll() {
        return service.getAll();
    }

    public void delete(int id) {
        //TODO MenuRestController userID
        service.delete(id, 100000);
    }
}
