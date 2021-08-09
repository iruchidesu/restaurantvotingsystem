package ru.iruchidesu.restaurantvotingsystem.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.service.VoteService;
import ru.iruchidesu.restaurantvotingsystem.web.SecurityUtil;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    static final String REST_URL = "/rest/profile/vote";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Vote> create(@RequestParam int restaurantId) {
        log.info("create with restaurantId {} by user with id {}", restaurantId, SecurityUtil.authUserId());
        Vote created = service.create(restaurantId, SecurityUtil.authUserId());
        return ResponseEntity.ok().body(created);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestParam int restaurantId) {
        log.info("update (new restaurantId {}) by user with id {}", restaurantId, SecurityUtil.authUserId());
        service.update(restaurantId, LocalTime.now(), SecurityUtil.authUserId());
    }

    @GetMapping
    public Vote get() {
        log.info("get today for user with id {}", SecurityUtil.authUserId());
        return service.getTodayVoteByUser(SecurityUtil.authUserId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete today for user with id {}", SecurityUtil.authUserId());
        service.delete(id, SecurityUtil.authUserId());
    }

    @GetMapping("/all")
    public List<Vote> getAllVoteByUser() {
        log.info("get all for user with id {}", SecurityUtil.authUserId());
        return service.getAllVoteByUser(SecurityUtil.authUserId());
    }
}
