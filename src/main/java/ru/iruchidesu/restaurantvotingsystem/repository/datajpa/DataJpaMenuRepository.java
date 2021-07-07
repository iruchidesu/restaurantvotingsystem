package ru.iruchidesu.restaurantvotingsystem.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.model.Role;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.repository.MenuRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaMenuRepository implements MenuRepository {

    private final CrudMenuRepository menuRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMenuRepository(CrudMenuRepository menuRepository, CrudUserRepository userRepository) {
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Menu save(Menu menu, int userId) {
        User user = userRepository.getById(userId);
        if (user.getRoles().contains(Role.ADMIN)) {
            return menuRepository.save(menu);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        User user = userRepository.getById(userId);
        return user.getRoles().contains(Role.ADMIN) && menuRepository.delete(id) != 0;
    }

    @Override
    public Menu get(int id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public Menu getTodayMenu(int restaurantId) {
        return menuRepository.getTodayMenuByRestaurantId(restaurantId);
    }

    @Override
    public List<Menu> getMenusByRestaurant(Restaurant restaurant) {
        return menuRepository.findMenuByRestaurantOrderByLocalDateDesc(restaurant);
    }
}
