package com.paweljurkiewicz.carrental.car_rental_facility;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarRentalService {

    private final CarRentalRepository repository;

    public CarRentalService(CarRentalRepository repository) {
        this.repository = repository;
    }

    CarRentalModel save(CarRentalModel carRentalModel) {
        return repository.save(carRentalModel);
    }

    CarRentalModel getById(Long id) {
        

        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Car Rental not found"));

    }
}