package ru.iruchidesu.restaurantvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.iruchidesu.restaurantvotingsystem.AuthorizedUser;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.repository.UserRepository;
import ru.iruchidesu.restaurantvotingsystem.to.UserTo;
import ru.iruchidesu.restaurantvotingsystem.util.UserUtil;

import java.util.List;
import java.util.Optional;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.*;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(user);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public User get(int id) {
        return repository.findById(id).orElseThrow(notFoundException("user with id = " + id));
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email).orElse(null), "email=" + email);
    }

    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        prepareAndSave(user);
    }

    @Transactional
    public void update(UserTo userTo) {
        User user = get(userTo.id());
        User updatedUser = UserUtil.updateFromTo(user, userTo);
        prepareAndSave(updatedUser);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Authenticating '{}'", email);
        Optional<User> optionalUser = repository.getByEmail(email);
        return new AuthorizedUser(optionalUser.orElseThrow(
                () -> new UsernameNotFoundException("User '" + email + "' was not found")));
    }

    public List<User> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    protected User prepareAndSave(User user) {
        return repository.save(UserUtil.prepareToSave(user));
    }
}
