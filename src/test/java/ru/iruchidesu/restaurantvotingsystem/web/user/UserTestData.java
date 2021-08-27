package ru.iruchidesu.restaurantvotingsystem.web.user;

import ru.iruchidesu.restaurantvotingsystem.MatcherFactory;
import ru.iruchidesu.restaurantvotingsystem.model.Role;
import ru.iruchidesu.restaurantvotingsystem.model.User;
import ru.iruchidesu.restaurantvotingsystem.to.UserTo;
import ru.iruchidesu.restaurantvotingsystem.util.json.JsonUtil;

import java.util.Collections;
import java.util.Date;

import static ru.iruchidesu.restaurantvotingsystem.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");
    public static final MatcherFactory.Matcher<UserTo> MATCHER_TO = MatcherFactory.usingIgnoringFieldsComparator(UserTo.class, "password");

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;

    public static final String USER_MAIL = "user@yandex.ru";
    public static final String ADMIN_MAIL = "admin@gmail.com";

    public static final User user = new User(USER_ID, "User", USER_MAIL, "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }


    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
