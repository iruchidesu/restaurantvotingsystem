package ru.iruchidesu.restaurantvotingsystem.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.model.Role;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.repository.RestaurantRepository;

import java.util.List;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final CrudRestaurantRepository restaurantRepository;
    private final CrudUserRepository userRepository;

    public DataJpaRestaurantRepository(CrudRestaurantRepository restaurantRepository, CrudUserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant, int userId) {
        User user = userRepository.getById(userId);
        if (user.getRoles().contains(Role.ADMIN)) {
            return restaurantRepository.save(restaurant);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        User user = userRepository.getById(userId);
        return user.getRoles().contains(Role.ADMIN) && restaurantRepository.delete(id) != 0;
    }

    @Override
    public Restaurant get(int id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    @Override
    public Restaurant getByName(String name) {
        return restaurantRepository.getByName(name);
    }

    @Override
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(SORT_NAME);
    }
}
