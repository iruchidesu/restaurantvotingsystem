package ru.iruchidesu.restaurantvotingsystem.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.service.RestaurantService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Restaurant Controller")
public class RestaurantController {

    static final String REST_URL = "/rest/restaurant";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Create new restaurant (only admin)")
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = service.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Update restaurant (only admin)")
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {}", restaurant);
        assureIdConsistent(restaurant, id);
        service.update(restaurant);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @GetMapping
    @Operation(summary = "Get all restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @GetMapping("/by")
    @Operation(summary = "Get restaurant by name")
    public Restaurant getByName(@RequestParam String name) {
        log.info("getByName {}", name);
        return service.getByName(name);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Delete restaurant (only admin)")
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    @GetMapping("/with-today-menu")
    @Operation(summary = "Get all restaurants with today's menu")
    public List<Restaurant> getWithMenu() {
        log.info("get all with today's menus");
        return service.getWithMenu();
    }
}
