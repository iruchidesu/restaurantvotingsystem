package ru.iruchidesu.restaurantvotingsystem.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.repository.MenuRepository;
import ru.iruchidesu.restaurantvotingsystem.repository.RestaurantRepository;
import ru.iruchidesu.restaurantvotingsystem.to.MenuTo;

import java.time.LocalDate;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @CacheEvict(value = "menu", allEntries = true)
    public Menu create(MenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menu must not be null");
        Menu menu = new Menu(null, LocalDate.now(), menuTo.getDishes());
        Restaurant restaurant = restaurantRepository.get(restaurantId);
        menu.setRestaurant(restaurant);
        return menuRepository.save(menu);
    }

    @CacheEvict(value = "menu", allEntries = true)
    public void deleteTodayMenu(int restaurantId) {
        checkNotFoundWithId(menuRepository.deleteByDate(restaurantId, LocalDate.now()), restaurantId);
    }

    public Menu get(int id) {
        return checkNotFoundWithId(menuRepository.get(id), id);
    }

    @CacheEvict(value = "menu", allEntries = true)
    public void update(MenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menu must not be null");
        Menu menu = getTodayMenu(restaurantId);
        menu.setDishes(menuTo.getDishes());
        checkNotFoundWithId(menuRepository.save(menu), menu.id());
    }

    @Cacheable(value = "menu", key = "#restaurantId + '_' + T(java.time.LocalDate).now().toString()")
    public Menu getTodayMenu(int restaurantId) {
        return checkNotFoundWithId(menuRepository.getMenuByDate(restaurantId, LocalDate.now()), restaurantId);
    }

    public List<Menu> getHistoryMenu(int restaurantId) {
        return menuRepository.getMenusByRestaurant(restaurantId);
    }
}
