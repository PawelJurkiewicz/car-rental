package com.paweljurkiewicz.carrental.reservation;


public class RentAlreadyExistsForReservation extends RuntimeException {

    public RentAlreadyExistsForReservation(String message) {
        super(message);
    }
}
