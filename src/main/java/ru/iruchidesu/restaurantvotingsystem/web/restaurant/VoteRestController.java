package ru.iruchidesu.restaurantvotingsystem.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.service.VoteService;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    static final String REST_URL = "/rest/restaurant";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteService service;

    public VoteRestController(VoteService service) {
        this.service = service;
    }

    @PostMapping("/{restaurantId}/vote")
    public ResponseEntity<Vote> create(@PathVariable int restaurantId) {
        //TODO VoteRestController userID authorized create
        log.info("create with restaurantId {} by user with id {}", restaurantId, 100000);
        Vote created = service.create(restaurantId, 100000);
        return ResponseEntity.ok().body(created);
    }

    @PutMapping(value = "/{restaurantId}/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId) {
        //TODO VoteRestController userID authorized update
        log.info("update (new restaurantId {}) by user with id {}", restaurantId, 100000);
        service.update(restaurantId, LocalTime.now(), 100000);
    }

    @GetMapping("/vote/{id}")
    public Vote get(@PathVariable int id) {
        log.info("get with id {}", id);
        return service.get(id);
    }

    @GetMapping("/vote/all")
    public List<Vote> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @DeleteMapping("/vote/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        //TODO VoteRestController userID authorized delete
        log.info("delete today for user with id {}", 100000);
        service.delete(id, 100000);
    }

    @GetMapping("/vote")
    public List<Vote> getAllVoteByUser() {
        //TODO VoteRestController userID authorized getAllVote
        log.info("get all for user with id {}", 100000);
        return service.getAllVoteByUser(100000);
    }
}
