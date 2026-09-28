package com.paweljurkiewicz.carrental.reservation;

import com.paweljurkiewicz.carrental.car_rental_facility.BranchesRepository;
import com.paweljurkiewicz.carrental.car_rental_facility.BranchesModel;
import com.paweljurkiewicz.carrental.car_rental_facility.ObjectNotFoundInRepositoryException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BranchesRepository branchesRepository;
    private final CarRepository carRepository;

    public ReservationService(ReservationRepository repository, BranchesRepository branchesRepository, CarRepository carRepository) {
        this.reservationRepository = repository;
        this.branchesRepository = branchesRepository;
        this.carRepository = carRepository;
    }

    public ReservationModel saveReservation(ReservationDTO reservationDTO) {
        if (reservationDTO.startDate().isAfter(reservationDTO.endDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        if (reservationRepository.existsOverlappingReservation(
                reservationDTO.carId(),
                reservationDTO.startDate(),
                reservationDTO.endDate())) {
            throw new CarNotAvailableException("Car with ID " + reservationDTO.carId() + " is already reserved in the specified time frame");
        }

        ReservationModel reservation = new ReservationModel();

        setStartAndEndBranch(reservationDTO, reservation);

        reservation.setCustomer(reservationDTO.customer());
        reservation.setStartDate(reservationDTO.startDate());
        reservation.setEndDate(reservationDTO.endDate());

        CarModel carFromRepo = carRepository.findById(reservationDTO.carId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Car not found"));

        reservation.setCar(carFromRepo);

        long daysDifference = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        if (daysDifference == 0) {
            daysDifference = 1;
        }
        BigDecimal price = carFromRepo.getPrice().multiply(new BigDecimal(daysDifference));
        reservation.setPrice(price);

        return reservationRepository.save(reservation);
    }

    private void setStartAndEndBranch(ReservationDTO reservationDTO, ReservationModel reservation) {
        BranchesModel startBranchFromRepo = branchesRepository.findById(reservationDTO.startBranchId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Start branch not found"));
        reservation.setStartBranch(startBranchFromRepo);

        BranchesModel endBranchFromRepo = branchesRepository.findById(reservationDTO.endBranchId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("End branch not found"));
        reservation.setEndBranch(endBranchFromRepo);
    }
}
