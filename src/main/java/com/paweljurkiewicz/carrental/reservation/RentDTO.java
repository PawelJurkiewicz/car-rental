package com.paweljurkiewicz.carrental.reservation;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

record RentDTO(
        @NotNull String employee,
        String comments,
        @NotNull LocalDate rentDate,
        @NotNull Long reservationId
) {
}
