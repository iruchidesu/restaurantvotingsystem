package ru.iruchidesu.restaurantvotingsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.iruchidesu.restaurantvotingsystem.error.AppException;
import ru.iruchidesu.restaurantvotingsystem.error.NotFoundException;
import ru.iruchidesu.restaurantvotingsystem.error.VoteUpdateTimeException;
import ru.iruchidesu.restaurantvotingsystem.util.ValidationUtil;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT = "A restaurant with the same name already exists";
    public static final String EXCEPTION_DUPLICATE_MENU = "Today's menu for this restaurant already exists";
    public static final String EXCEPTION_DUPLICATE_DISH = "Duplicate dish names in this menu";
    public static final String EXCEPTION_DUPLICATE_VOTE = "Today's vote for this user already exists";
    public static final String EXCEPTION_UPDATE_VOTE = "It is too late to change your vote";

    private static final Map<String, String> CONSTRAINS_MAP = Map.of(
            "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "restaurant_unique_name_idx", EXCEPTION_DUPLICATE_RESTAURANT,
            "menu_restaurant_date_idx", EXCEPTION_DUPLICATE_MENU,
            "menu_dish_idx", EXCEPTION_DUPLICATE_DISH,
            "user_voting_date_idx", EXCEPTION_DUPLICATE_VOTE);

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ErrorAttributes errorAttributes;

    public GlobalExceptionHandler(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appException(WebRequest request, AppException ex) {
        log.error("ApplicationException", ex);
        return createResponseEntity(getDefaultBody(request, ex.getOptions(), null), ex.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> persistException(WebRequest request, NotFoundException ex) {
        log.error("NotFoundException ", ex);
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(MESSAGE), ex.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(VoteUpdateTimeException.class)
    public ResponseEntity<?> handleError(WebRequest request, VoteUpdateTimeException ex) {
        log.error("VoteUpdateTimeException ", ex);
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(MESSAGE), EXCEPTION_UPDATE_VOTE),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> conflict(WebRequest request, DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException ", ex);
        String rootMsg = ValidationUtil.getRootCause(ex).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINS_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(MESSAGE), entry.getValue()),
                            HttpStatus.CONFLICT);
                }
            }
        }
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.of(MESSAGE), null),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ResponseEntity<Object> handleBindingErrors(BindingResult result, WebRequest request) {
        String msg = result.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining("\n"));
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.defaults(), msg),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private Map<String, Object> getDefaultBody(WebRequest request, ErrorAttributeOptions options, String msg) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, options);
        if (msg != null) {
            body.put("message", msg);
        }
        return body;
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<T> createResponseEntity(Map<String, Object> body, HttpStatus status) {
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return (ResponseEntity<T>) ResponseEntity.status(status).body(body);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex, Object body, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("Exception", ex);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
