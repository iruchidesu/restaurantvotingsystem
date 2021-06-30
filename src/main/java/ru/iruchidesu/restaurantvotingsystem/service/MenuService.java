package ru.iruchidesu.restaurantvotingsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.repository.MenuRepository;

import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService {
    private final MenuRepository repository;

    public MenuService(MenuRepository repository) {
        this.repository = repository;
    }

    public Menu create(Menu menu, int userId) {
        Assert.notNull(menu, "menu must not be null");
        return repository.save(menu, userId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Menu get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Menu> getAll() {
        return repository.getAll();
    }

    public void update(Menu menu, int userId) {
        Assert.notNull(menu, "menu must not be null");
        checkNotFoundWithId(repository.save(menu, userId), menu.id());
    }
}
