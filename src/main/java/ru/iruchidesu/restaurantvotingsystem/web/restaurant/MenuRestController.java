package ru.iruchidesu.restaurantvotingsystem.web.restaurant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.service.MenuService;
import ru.iruchidesu.restaurantvotingsystem.to.MenuTo;

import java.net.URI;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = MenuRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestController {

    static final String REST_URL = "/rest/restaurant";

    private final MenuService service;

    public MenuRestController(MenuService service) {
        this.service = service;
    }

    @PostMapping(value = "/{restaurantId}/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> create(@RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        Menu created = service.create(menuTo, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{restaurantId}/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        //TODO MenuRestController authorized userID
        service.update(menuTo, restaurantId);
    }

    @GetMapping("/{restaurantId}/menu")
    public Menu getTodayMenu(@PathVariable int restaurantId) {
        return service.getTodayMenu(restaurantId);
    }

    @GetMapping("/{restaurantId}/menu/history")
    public List<Menu> getHistoryMenu(@PathVariable int restaurantId) {
        return service.getHistoryMenu(restaurantId);
    }

    @DeleteMapping("/menu/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        //TODO MenuRestController authorized userID
        service.delete(id, 100000);
    }
}
