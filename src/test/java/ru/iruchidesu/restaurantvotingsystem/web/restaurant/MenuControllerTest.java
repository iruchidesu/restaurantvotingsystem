package ru.iruchidesu.restaurantvotingsystem.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.error.NotFoundException;
import ru.iruchidesu.restaurantvotingsystem.model.Dish;
import ru.iruchidesu.restaurantvotingsystem.model.Menu;
import ru.iruchidesu.restaurantvotingsystem.service.MenuService;
import ru.iruchidesu.restaurantvotingsystem.util.MenuUtil;
import ru.iruchidesu.restaurantvotingsystem.util.json.JsonUtil;
import ru.iruchidesu.restaurantvotingsystem.web.AbstractControllerTest;
import ru.iruchidesu.restaurantvotingsystem.web.GlobalExceptionHandler;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.MenuTestData.*;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static ru.iruchidesu.restaurantvotingsystem.web.user.UserTestData.ADMIN_MAIL;
import static ru.iruchidesu.restaurantvotingsystem.web.user.UserTestData.USER_MAIL;

class MenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/rest/restaurant/";

    @Autowired
    private MenuService menuService;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        Menu newMenu = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT2_ID + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.asTo(newMenu))))
                .andExpect(status().isCreated());

        Menu created = MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        MATCHER.assertMatch(created, newMenu);
        MATCHER.assertMatch(menuService.get(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Menu updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.asTo(updated))))
                .andDo(print())
                .andExpect(status().isNoContent());

        MATCHER.assertMatch(menuService.get(MENU_TODAY_R1_ID), updated);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getTodayMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/menu"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(MenuUtil.asTo(menuTodayR1)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT2_ID + "/menu"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getHistoryMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/menu/history"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(menuTodayR1, menu2, menu1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID + "/menu"))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> menuService.get(RESTAURANT1_ID));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createForbidden() throws Exception {
        Menu newMenu = getNew();
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT2_ID + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.asTo(newMenu))))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateForbidden() throws Exception {
        Menu updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.asTo(updated))))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID + "/menu"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Menu newMenu = new Menu(null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT2_ID + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.asTo(newMenu))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Menu updated = getUpdated();
        updated.setDishes(null);
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.asTo(updated))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Menu newMenu = getNew();
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.asTo(newMenu))))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_MENU)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicateDish() throws Exception {
        Menu newMenu = getNew();
        newMenu.setDishes(List.of(new Dish("duplicate", 1000), new Dish("duplicate", 1500)));
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT2_ID + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.asTo(newMenu))))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_DISH)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicateDish() throws Exception {
        Menu updated = getUpdated();
        updated.setDishes(List.of(new Dish("duplicate", 1000), new Dish("duplicate", 1500)));
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MenuUtil.asTo(updated))))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_DISH)));
    }
}
