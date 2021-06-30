package ru.iruchidesu.restaurantvotingsystem.util.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}