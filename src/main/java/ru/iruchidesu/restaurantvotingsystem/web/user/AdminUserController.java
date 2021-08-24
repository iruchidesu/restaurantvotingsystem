package ru.iruchidesu.restaurantvotingsystem.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iruchidesu.restaurantvotingsystem.model.User;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin Controller")
public class AdminUserController extends AbstractUserController {

    static final String REST_URL = "/rest/admin/users";

    @Override
    @GetMapping
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Get all users (only admin)")
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Get user by id (only admin)")
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Create new user (only admin)")
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
        User created = super.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Delete user (only admin)")
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Update user (only admin)")
    public void update(@Valid @RequestBody User user, @PathVariable int id) {
        super.update(user, id);
    }

    @Override
    @GetMapping("/by")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Get user by email (only admin)")
    public User getByMail(@RequestParam String email) {
        return super.getByMail(email);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Enable/disable user (only admin)")
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        service.enable(id, enabled);
    }
}
