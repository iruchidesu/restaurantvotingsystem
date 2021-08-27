package ru.iruchidesu.restaurantvotingsystem.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.service.UserService;
import ru.iruchidesu.restaurantvotingsystem.to.UserTo;
import ru.iruchidesu.restaurantvotingsystem.util.UserUtil;
import ru.iruchidesu.restaurantvotingsystem.util.json.JsonUtil;
import ru.iruchidesu.restaurantvotingsystem.web.AbstractControllerTest;
import ru.iruchidesu.restaurantvotingsystem.web.GlobalExceptionHandler;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iruchidesu.restaurantvotingsystem.web.user.ProfileController.REST_URL;
import static ru.iruchidesu.restaurantvotingsystem.web.user.UserTestData.*;

class ProfileControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_TO.contentJson(UserUtil.asTo(user)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isNoContent());
        MATCHER.assertMatch(userService.getAll(), admin);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MATCHER.assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(user), updatedTo));
    }

    @Test
    void signUp() throws Exception {
        User newUser = getNew();
        newUser.setEnabled(true);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(UserUtil.asTo(newUser))))
                .andExpect(status().isCreated());

        User created = MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        MATCHER.assertMatch(created, newUser);
        MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void signUpInvalid() throws Exception {
        UserTo newTo = new UserTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL + "/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalid() throws Exception {
        UserTo updatedTo = new UserTo(null, null, "password", null);
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", ADMIN_MAIL, "newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL)));
    }
}
