package ru.iruchidesu.restaurantvotingsystem.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.repository.RestaurantRepository;

import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNotFound;
import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {
    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "restaurant", allEntries = true)
    public Restaurant create(Restaurant restaurant, int userId) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant, userId);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "restaurant", allEntries = true),
                    @CacheEvict(value = "menu", key = "#id + '_' + T(java.time.LocalDate).now().toString()")
            }
    )
    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public Restaurant getByName(String name) {
        Assert.notNull(name, "name must not be null");
        return checkNotFound(repository.getByName(name), "name=" + name);
    }

    @Cacheable("restaurant")
    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    @CacheEvict(value = "restaurant", allEntries = true)
    public void update(Restaurant restaurant, int userId) {
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFoundWithId(repository.save(restaurant, userId), restaurant.id());
    }
}
