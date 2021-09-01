package ru.iruchidesu.restaurantvotingsystem.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil.*;

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
                    @CacheEvict(value = "restaurant", key = "#id"),
                    @CacheEvict(value = "menu", allEntries = true)
            }
    )
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return repository.findById(id).orElseThrow(notFoundRestaurantException(id));
    }

    public Restaurant getByName(String name) {
        Assert.notNull(name, "name must not be null");
        return repository.getByNameContainingIgnoreCaseOrderByNameAsc(name).orElseThrow(notFoundException("restaurant with name = " + name));
    }

    @Cacheable("restaurant")
    public List<Restaurant> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @CachePut(value = "restaurant", key = "#restaurant.id")
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFoundWithId(repository.save(restaurant), restaurant.id());
    }

    public List<Restaurant> getWithMenu() {
        return repository.getWithTodayMenu(LocalDate.now());
    }
}
