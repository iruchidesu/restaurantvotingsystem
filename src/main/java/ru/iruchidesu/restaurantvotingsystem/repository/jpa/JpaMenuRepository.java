package ru.iruchidesu.restaurantvotingsystem.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.model.Role;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.repository.MenuRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMenuRepository implements MenuRepository {

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Menu save(Menu menu, int userId) {
        User user = em.find(User.class, userId);
        if (user.getRoles().contains(Role.ADMIN)) {
            if (menu.isNew()) {
                em.persist(menu);
                return menu;
            } else {
                return em.merge(menu);
            }
        }
        return null;
    }
    //TODO implements Menu delete
    @Override
    public boolean delete(int id, int userId) {
        return false;
    }

    @Override
    public Menu get(int id) {
        return em.find(Menu.class, id);
    }
    //TODO implements Menu getAll
    @Override
    public List<Menu> getAll() {
        return null;
    }
}
