package ru.iruchidesu.restaurantvotingsystem.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.repository.MenuRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaMenuRepository implements MenuRepository {

    private final CrudMenuRepository menuRepository;

    public DataJpaMenuRepository(CrudMenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    @Transactional
    public Menu save(Menu menu) {
        if (menu.getLocalDate().isEqual(LocalDate.now())) {
            return menuRepository.save(menu);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteByDate(int restaurantId, LocalDate localDate) {
        return menuRepository.delete(restaurantId, localDate) != 0;
    }

    @Override
    public Menu get(int id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public Menu getMenuByDate(int restaurantId, LocalDate localDate) {
        return menuRepository.getMenuByRestaurantIdAndLocalDate(restaurantId, localDate);
    }

    @Override
    public List<Menu> getMenusByRestaurant(int restaurantId) {
        return menuRepository.getMenuByRestaurantIdOrderByLocalDateDesc(restaurantId);
    }
}
