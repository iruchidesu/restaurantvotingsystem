package ru.iruchidesu.restaurantvotingsystem.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.service.MenuService;
import ru.iruchidesu.restaurantvotingsystem.to.MenuTo;
import ru.iruchidesu.restaurantvotingsystem.util.ToUtil;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = MenuRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestController {

    static final String REST_URL = "/rest/restaurant/{restaurantId}/menu";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MenuService service;

    public MenuRestController(MenuService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> create(@RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("create for restaurant with id {}", restaurantId);
        Menu created = service.create(menuTo, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("update for restaurant with id {}", restaurantId);
        service.update(menuTo, restaurantId);
    }

    @GetMapping
    public MenuTo getTodayMenu(@PathVariable int restaurantId) {
        log.info("get today for restaurant with id {}", restaurantId);
        return ToUtil.getMenuTo(service.getTodayMenu(restaurantId));
    }

    @GetMapping("/history")
    public List<Menu> getHistoryMenu(@PathVariable int restaurantId) {
        log.info("get all for restaurant with id {}", restaurantId);
        return service.getHistoryMenu(restaurantId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId) {
        log.info("delete today for restaurant with id {}", restaurantId);
        service.deleteTodayMenu(restaurantId);
    }
}
