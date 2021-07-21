package ru.iruchidesu.restaurantvotingsystem.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id AND m.localDate=CURRENT_DATE")
    int delete(@Param("id") int id);

    @Query("SELECT m FROM Menu m WHERE m.localDate=CURRENT_DATE AND m.restaurant.id=:restaurantId")
    Menu getTodayMenuByRestaurantId(@Param("restaurantId") int restaurantId);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.restaurant.id=:restaurantId ORDER BY m.localDate DESC")
    List<Menu> getMenuByRestaurantIdOrderByLocalDateDesc(@Param("restaurantId") int restaurantId);
}
