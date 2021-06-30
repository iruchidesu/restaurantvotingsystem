package ru.iruchidesu.restaurantvotingsystem.web.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.service.UserService;

import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNew;

@Controller
public class ProfileRestController {

    @Autowired
    private UserService service;

    public User create(User user) {
        checkNew(user);
        return service.create(user);
    }

    public void update(User user, int id) {
        assureIdConsistent(user, id);
        service.update(user);
    }

    public User get() {
        //TODO ProfileRestController ID get
        return service.get(100000);
    }

    public List<User> getAll() {
        return service.getAll();
    }

    public User getByMail(String email) {
        return service.getByEmail(email);
    }

    public void delete() {
        //TODO ProfileRestController ID delete
        service.delete(100000);
    }
}
