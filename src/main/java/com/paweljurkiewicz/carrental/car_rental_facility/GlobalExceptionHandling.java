package com.paweljurkiewicz.carrental.car_rental_facility;

import com.paweljurkiewicz.carrental.reservation.CarNotAvailableException;
import com.paweljurkiewicz.carrental.reservation.RentAlreadyExistsForReservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(ObjectNotFoundInRepositoryException.class)
    public ProblemDetail handleObjectNotFoundInRepository(ObjectNotFoundInRepositoryException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(CarNotAvailableException.class)
    public ProblemDetail handleCarNotAvailable(CarNotAvailableException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(RentAlreadyExistsForReservation.class)
    public ProblemDetail handleRentAlreadyExistsForReservation(RentAlreadyExistsForReservation exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        List<String> errors = fieldErrors
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .toList();

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors.toString());
    }
}
