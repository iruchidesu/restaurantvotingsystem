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
import ru.iruchidesu.restaurantvotingsystem.model.Restaurant;
import ru.iruchidesu.restaurantvotingsystem.service.RestaurantService;
import ru.iruchidesu.restaurantvotingsystem.util.json.JsonUtil;
import ru.iruchidesu.restaurantvotingsystem.web.AbstractControllerTest;
import ru.iruchidesu.restaurantvotingsystem.web.GlobalExceptionHandler;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.*;
import static ru.iruchidesu.restaurantvotingsystem.UserTestData.ADMIN_MAIL;
import static ru.iruchidesu.restaurantvotingsystem.UserTestData.USER_MAIL;

class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantService restaurantService;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        Restaurant created = MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        MATCHER.assertMatch(created, newRestaurant);
        MATCHER.assertMatch(restaurantService.get(newId), newRestaurant);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MATCHER.assertMatch(restaurantService.get(RESTAURANT1_ID), updated);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT2_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(restaurant2));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(restaurant1, restaurant2));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by?name=" + restaurant1.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getWithMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "with-today-menu"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_WITH_MENU.contentJson(List.of(restaurant1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT2_ID))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantService.get(RESTAURANT2_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createForbidden() throws Exception {
        Restaurant newRestaurant = getNew();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateForbidden() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT2_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, "", null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant updated = getUpdated();
        updated.setAddress("");
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, restaurant1.getName(), "address_rest3");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_RESTAURANT)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT1_ID, restaurant2.getName(), "address_rest1");
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_RESTAURANT)));
    }
}
