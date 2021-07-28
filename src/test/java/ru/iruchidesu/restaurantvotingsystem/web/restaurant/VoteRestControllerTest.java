package ru.iruchidesu.restaurantvotingsystem.web.restaurant;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.iruchidesu.restaurantvotingsystem.VoteTestData;
import ru.iruchidesu.restaurantvotingsystem.model.Vote;
import ru.iruchidesu.restaurantvotingsystem.service.VoteService;
import ru.iruchidesu.restaurantvotingsystem.util.exception.NotFoundException;
import ru.iruchidesu.restaurantvotingsystem.web.AbstractControllerTest;
import ru.iruchidesu.restaurantvotingsystem.web.json.JsonUtil;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.RESTAURANT2_ID;
import static ru.iruchidesu.restaurantvotingsystem.RestaurantTestData.restaurant2;
import static ru.iruchidesu.restaurantvotingsystem.VoteTestData.*;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteService voteService;

    @Test
    void create() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "restaurant/" + RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andExpect(status().isCreated());

        Vote created = MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        newVote.setRestaurant(restaurant2);
        MATCHER.assertMatch(created, newVote);
        MATCHER.assertMatch(voteService.get(newId), newVote);
    }

    @Test
    void update() throws Exception {
        Assumptions.assumeTrue(LocalTime.now().isBefore(LocalTime.of(11, 0)));
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE_TODAY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MATCHER.assertMatch(voteService.get(VOTE_TODAY1_ID), updated);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE_TODAY1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(voteToday1));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(all));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE_TODAY1_ID))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> voteService.get(VOTE_TODAY1_ID));
    }

    @Test
    void getAllVoteByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(voteToday1, vote1));
    }
}
