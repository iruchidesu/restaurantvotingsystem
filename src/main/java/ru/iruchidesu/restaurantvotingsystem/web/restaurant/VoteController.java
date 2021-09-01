package ru.iruchidesu.restaurantvotingsystem.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.service.VoteService;
import ru.iruchidesu.restaurantvotingsystem.web.SecurityUtil;

import javax.validation.Valid;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vote Controller")
public class VoteController {

    static final String REST_URL = "/rest/profile/votes";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteService service;

    private final Clock clock;

    public VoteController(VoteService service, Clock clock) {
        this.service = service;
        this.clock = clock;
    }

    @PostMapping
    @Operation(summary = "Vote for restaurant")
    public ResponseEntity<Vote> create(@Valid @RequestBody int restaurantId) {
        log.info("create with restaurantId {} by user with id {}", restaurantId, SecurityUtil.authUserId());
        Vote created = service.create(restaurantId, SecurityUtil.authUserId());
        return ResponseEntity.ok().body(created);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update vote")
    public void update(@Valid @RequestBody int restaurantId) {
        log.info("update (new restaurantId {}) by user with id {}", restaurantId, SecurityUtil.authUserId());
        service.update(restaurantId, LocalTime.now(clock), SecurityUtil.authUserId());
    }

    @GetMapping("/by")
    @Operation(summary = "Get vote for auth user on date (yyyy-MM-dd)")
    public Vote get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        log.info("get for user with id {}", SecurityUtil.authUserId());
        return service.getVoteByUser(SecurityUtil.authUserId(), date);
    }

    @GetMapping
    @Operation(summary = "Get all votes for auth user")
    public List<Vote> getAllVoteByUser() {
        log.info("get all for user with id {}", SecurityUtil.authUserId());
        return service.getAllVoteByUser(SecurityUtil.authUserId());
    }
}
