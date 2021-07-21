package ru.iruchidesu.restaurantvotingsystem.web.Restaurant;

import org.springframework.stereotype.Controller;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.service.VoteService;

import java.time.LocalTime;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNew;

@Controller
public class VoteRestController {
    private final VoteService service;

    public VoteRestController(VoteService service) {
        this.service = service;
    }

    public Vote create(Vote vote, int restaurantId) {
        checkNew(vote);
        //TODO VoteRestController userID authorized create
        return service.create(vote, restaurantId, 100001);
    }

    public void update(Vote vote, int id) {
        //TODO VoteRestController userID authorized update
        assureIdConsistent(vote, id);
        service.update(vote, LocalTime.now(), 100001);
    }

    public Vote get(int id) {
        return service.get(id);
    }

    public List<Vote> getAll() {
        return service.getAll();
    }

    public void delete(int id) {
        //TODO VoteRestController userID authorized delete
        service.delete(id, 100001);
    }

    public List<Vote> getAllVoteByUser() {
        //TODO VoteRestController userID authorized getAllVote
        return service.getAllVoteByUser(100001);
    }
}
