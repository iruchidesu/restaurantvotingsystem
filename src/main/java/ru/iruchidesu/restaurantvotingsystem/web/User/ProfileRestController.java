package ru.iruchidesu.restaurantvotingsystem.web.User;

import org.springframework.stereotype.Controller;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.service.UserService;
import ru.iruchidesu.restaurantvotingsystem.to.UserTo;
import ru.iruchidesu.restaurantvotingsystem.util.ToUtil;

import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNew;

@Controller
public class ProfileRestController {

    private final UserService service;

    public ProfileRestController(UserService service) {
        this.service = service;
    }

    public User create(User user) {
        checkNew(user);
        return service.create(user);
    }

    public void update(User user, int id) {
        assureIdConsistent(user, id);
        service.update(user);
    }

    public UserTo get() {
        //TODO ProfileRestController authorized ID get
        return ToUtil.getUserTo(service.get(100000));
    }

    public List<UserTo> getAll() {
        return ToUtil.getUserTos(service.getAll());
    }

    public UserTo getByEmail(String email) {
        return ToUtil.getUserTo(service.getByEmail(email));
    }

    public void delete() {
        //TODO ProfileRestController ID delete
        service.delete(100000);
    }
}
