package ru.iruchidesu.restaurantvotingsystem.web.restaurant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.FixedClock;
import ru.iruchidesu.restaurantvotingsystem.error.NotFoundException;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.service.VoteService;
import ru.iruchidesu.restaurantvotingsystem.util.json.JsonUtil;
import ru.iruchidesu.restaurantvotingsystem.web.AbstractControllerTest;
import ru.iruchidesu.restaurantvotingsystem.web.GlobalExceptionHandler;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.RestaurantTestData.RESTAURANT2_ID;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.RestaurantTestData.restaurant2;
import static ru.iruchidesu.restaurantvotingsystem.web.restaurant.VoteTestData.*;
import static ru.iruchidesu.restaurantvotingsystem.web.user.UserTestData.ADMIN_MAIL;
import static ru.iruchidesu.restaurantvotingsystem.web.user.UserTestData.USER_MAIL;

@ExtendWith(MockitoExtension.class)
// https://dzone.com/articles/mockbean-spring-boots-missing-ingredient
@FixedClock
class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteController.REST_URL + '/';

    @Autowired
    private VoteService voteService;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID)))
                .andExpect(status().isOk());

        Vote created = MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        newVote.setRestaurant(restaurant2);
        MATCHER.assertMatch(created, newVote);
        MATCHER.assertMatch(voteService.get(newId), newVote);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MATCHER.assertMatch(voteService.get(VOTE_TODAY1_ID), updated);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @FixedClock("2021-08-17T12:03:00Z")
    void updateForbiddenTime() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_UPDATE_VOTE)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(voteToday1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> voteService.get(VOTE_TODAY1_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllVoteByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(voteToday1, vote1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RestaurantTestData.NOT_FOUND)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", String.valueOf(RestaurantTestData.NOT_FOUND)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = USER_MAIL)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_VOTE)));
    }
}
