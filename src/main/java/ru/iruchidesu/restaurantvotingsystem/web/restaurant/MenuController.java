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
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.service.MenuService;
import ru.iruchidesu.restaurantvotingsystem.to.MenuTo;
import ru.iruchidesu.restaurantvotingsystem.util.MenuUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Menu Controller")
public class MenuController {

    static final String REST_URL = "/rest/restaurant/{restaurantId}/menu";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Create today's menu (only admin)")
    public ResponseEntity<Menu> create(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("create for restaurant with id {}", restaurantId);
        Menu created = service.create(menuTo, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Update today's menu (only admin)")
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("update for restaurant with id {}", restaurantId);
        service.update(menuTo, restaurantId);
    }

    @GetMapping
    @Operation(summary = "Get today's menu for a restaurant")
    public MenuTo getTodayMenu(@PathVariable int restaurantId) {
        log.info("get today for restaurant with id {}", restaurantId);
        return MenuUtil.asTo(service.getTodayMenu(restaurantId));
    }

    @GetMapping("/history")
    @Operation(summary = "Get menu history for a restaurant")
    public List<Menu> getHistoryMenu(@PathVariable int restaurantId) {
        log.info("get all for restaurant with id {}", restaurantId);
        return service.getHistoryMenu(restaurantId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Delete today's menu for a restaurant (only admin)")
    public void delete(@PathVariable int restaurantId) {
        log.info("delete today for restaurant with id {}", restaurantId);
        service.deleteTodayMenu(restaurantId);
    }
}
