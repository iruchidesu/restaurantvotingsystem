package ru.iruchidesu.restaurantvotingsystem.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNotFoundWithId;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.notFoundException;

@Service
public class RestaurantService {
    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "restaurant", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "restaurant", allEntries = true),
                    @CacheEvict(value = "menu", key = "#id + '_' + T(java.time.LocalDate).now().toString()")
            }
    )
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return repository.findById(id).orElseThrow(notFoundException("restaurant with id = " + id));
    }

    public Restaurant getByName(String name) {
        Assert.notNull(name, "name must not be null");
        return repository.getByName(name).orElseThrow(notFoundException("restaurant with name = " + name));
    }

    @Cacheable("restaurant")
    public List<Restaurant> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @CacheEvict(value = "restaurant", allEntries = true)
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFoundWithId(repository.save(restaurant), restaurant.id());
    }

    public Restaurant getWithMenu(int id) {
        return repository.getWithTodayMenu(id, LocalDate.now()).orElseThrow(notFoundException("restaurant with id = " + id));
    }
}
