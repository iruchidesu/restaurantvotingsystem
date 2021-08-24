package ru.iruchidesu.restaurantvotingsystem.error;

public class IllegalRequestDataException extends RuntimeException {
    public IllegalRequestDataException(String msg) {
        super(msg);
    }
}