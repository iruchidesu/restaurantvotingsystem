package ru.iruchidesu.restaurantvotingsystem.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iruchidesu.restaurantvotingsystem.AuthorizedUser;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.to.UserTo;
import ru.iruchidesu.restaurantvotingsystem.util.UserUtil;

import javax.validation.Valid;
import java.net.URI;

import static ru.iruchidesu.restaurantvotingsystem.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController extends AbstractUserController {
    static final String REST_URL = "/rest/profile";

    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> signUp(@Valid @RequestBody UserTo userTo) {
        User created = super.create(UserUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserTo userTo) {
        super.update(userTo, authUserId());
    }

    @GetMapping
    public UserTo get(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        return authorizedUser.getUserTo();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(authUserId());
    }
}
