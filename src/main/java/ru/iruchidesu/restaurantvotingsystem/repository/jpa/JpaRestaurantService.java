package ru.iruchidesu.restaurantvotingsystem.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.model.Role;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.repository.RestaurantService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaRestaurantService implements RestaurantService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Restaurant save(Restaurant restaurant, int userId) {
        User user = em.find(User.class, userId);
        if (user.getRoles().contains(Role.ADMIN)) {
            if (restaurant.isNew()) {
                em.persist(restaurant);
                return restaurant;
            } else {
                return em.merge(restaurant);
            }
        }
        return null;
    }
    //TODO implements Restaurant delete
    @Override
    public boolean delete(int id, int userId) {
        return false;
    }

    @Override
    public Restaurant get(int id) {
        return em.find(Restaurant.class, id);
    }
    //TODO implements Restaurant getByName
    @Override
    public Restaurant getByName(String name) {
        return null;
    }
    //TODO implements Restaurant getAll
    @Override
    public List<Restaurant> getAll() {
        return null;
    }
}
