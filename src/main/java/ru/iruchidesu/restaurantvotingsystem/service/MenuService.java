package ru.iruchidesu.restaurantvotingsystem.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.repository.MenuRepository;
import ru.iruchidesu.restaurantvotingsystem.repository.RestaurantRepository;
import ru.iruchidesu.restaurantvotingsystem.to.MenuTo;

import java.time.LocalDate;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNotFoundWithId;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.notFoundException;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @CacheEvict(value = "menu", allEntries = true)
    @Transactional
    public Menu create(MenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menu must not be null");
        Menu menu = new Menu(null, LocalDate.now(), menuTo.getDishes());
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(notFoundException("restaurant with id = " + restaurantId));
        menu.setRestaurant(restaurant);
        return menuRepository.save(menu);
    }

    @CacheEvict(value = "menu", allEntries = true)
    public void deleteTodayMenu(int restaurantId) {
        checkNotFoundWithId(menuRepository.delete(restaurantId, LocalDate.now()) != 0, restaurantId);
    }

    public Menu get(int id) {
        return menuRepository.findById(id).orElseThrow(notFoundException("menu with id = " + id));
    }

    @CacheEvict(value = "menu", allEntries = true)
    @Transactional
    public void update(MenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menu must not be null");
        Menu menu = getTodayMenu(restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(notFoundException("restaurant with id = " + restaurantId));
        menu.setRestaurant(restaurant);
        menu.setDishes(menuTo.getDishes());
    }

    @Cacheable(value = "menu", key = "#restaurantId + '_' + T(java.time.LocalDate).now().toString()")
    public Menu getTodayMenu(int restaurantId) {
        return menuRepository.getMenuByRestaurantIdAndLocalDate(restaurantId, LocalDate.now())
                .orElseThrow(notFoundException("menu with restaurantId = " + restaurantId));
    }

    public List<Menu> getHistoryMenu(int restaurantId) {
        return menuRepository.getMenuByRestaurantIdOrderByLocalDateDesc(restaurantId);
    }
}
