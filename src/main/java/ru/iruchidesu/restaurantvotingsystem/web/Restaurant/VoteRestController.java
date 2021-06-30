package ru.iruchidesu.restaurantvotingsystem.web.Restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.service.VoteService;

import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNew;

@Controller
public class VoteRestController {
    @Autowired
    private VoteService service;

    public Vote create(Vote vote) {
        checkNew(vote);
        return service.create(vote);
    }

    public void update(Vote vote, int id) {
        assureIdConsistent(vote, id);
        service.update(vote);
    }

    public Vote get(int id) {
        return service.get(id);
    }

    public List<Vote> getAll() {
        return service.getAll();
    }

    public void delete(int id) {
        //TODO VoteRestController userID delete
        service.delete(id, 100001);
    }
}
