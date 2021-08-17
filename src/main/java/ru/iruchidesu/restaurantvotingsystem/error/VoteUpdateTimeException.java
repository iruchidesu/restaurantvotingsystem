package ru.iruchidesu.restaurantvotingsystem.error;

public class VoteUpdateTimeException extends RuntimeException {
    public VoteUpdateTimeException(String message) {
        super(message);
    }
}
