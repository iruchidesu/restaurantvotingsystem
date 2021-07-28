package ru.iruchidesu.restaurantvotingsystem.web.restaurant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.service.VoteService;

import java.net.URI;
import java.time.LocalTime;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    static final String REST_URL = "/rest/vote";

    private final VoteService service;

    public VoteRestController(VoteService service) {
        this.service = service;
    }

    @PostMapping(value = "/restaurant/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> create(@RequestBody Vote vote, @PathVariable int restaurantId) {
        checkNew(vote);
        //TODO VoteRestController userID authorized create
        Vote created = service.create(vote, restaurantId, 100000);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/restaurant/{restaurantId}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Vote vote, @PathVariable int id) {
        //TODO VoteRestController userID authorized update
        assureIdConsistent(vote, id);
        service.update(vote, LocalTime.now(), 100000);
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        return service.get(id);
    }

    @GetMapping("/all")
    public List<Vote> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        //TODO VoteRestController userID authorized delete
        service.delete(id, 100000);
    }

    @GetMapping
    public List<Vote> getAllVoteByUser() {
        //TODO VoteRestController userID authorized getAllVote
        return service.getAllVoteByUser(100000);
    }
}
