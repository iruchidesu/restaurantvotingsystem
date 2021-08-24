package ru.iruchidesu.restaurantvotingsystem.web.user;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.iruchidesu.restaurantvotingsystem.HasIdAndEmail;
import ru.iruchidesu.restaurantvotingsystem.repository.UserRepository;
import ru.iruchidesu.restaurantvotingsystem.web.GlobalExceptionHandler;
import ru.iruchidesu.restaurantvotingsystem.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {
    private final UserRepository repository;
    private final HttpServletRequest request;

    public UniqueMailValidator(UserRepository repository, HttpServletRequest request) {
        this.repository = repository;
        this.request = request;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            repository.getByEmail(user.getEmail().toLowerCase())
                    .ifPresent(dbUser -> {
                        if (request.getMethod().equals("PUT")) {  // UPDATE
                            int dbId = dbUser.id();

                            // it is ok, if update ourself
                            if (user.getId() != null && dbId == user.id()) return;

                            // Workaround for update with user.id=null in request body
                            // ValidationUtil.assureIdConsistent called after this validation
                            String requestURI = request.getRequestURI();
                            if (requestURI.endsWith("/" + dbId) || (dbId == SecurityUtil.authUserId() && requestURI.contains("/rest/profile")))
                                return;
                        }
                        errors.rejectValue("email", null, GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL);
                    });
        }
    }
}
