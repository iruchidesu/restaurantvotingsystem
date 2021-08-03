package ru.iruchidesu.restaurantvotingsystem.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.restaurant.id=:restaurantId AND m.localDate=:localDate")
    int delete(@Param("restaurantId") int restaurantId, @Param("localDate") LocalDate localDate);

    Menu getMenuByRestaurantIdAndLocalDate(int restaurantId, LocalDate localDate);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.restaurant.id=:restaurantId ORDER BY m.localDate DESC")
    List<Menu> getMenuByRestaurantIdOrderByLocalDateDesc(@Param("restaurantId") int restaurantId);
}
