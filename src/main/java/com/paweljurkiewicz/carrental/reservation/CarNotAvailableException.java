package com.paweljurkiewicz.carrental.reservation;

public class CarNotAvailableException extends RuntimeException {
    public CarNotAvailableException(String message) {
        super(message);
    }
}
