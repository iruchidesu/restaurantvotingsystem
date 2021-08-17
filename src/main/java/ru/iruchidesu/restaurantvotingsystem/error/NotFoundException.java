package ru.iruchidesu.restaurantvotingsystem.error;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}