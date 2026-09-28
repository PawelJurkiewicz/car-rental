package com.paweljurkiewicz.carrental.reservation;

import com.paweljurkiewicz.carrental.car_rental_facility.ObjectNotFoundInRepositoryException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentService {

    private final RentRepository rentRepository;

    private final ReservationRepository reservationRepository;

    public RentService(RentRepository rentRepository, ReservationRepository reservationRepository) {
        this.rentRepository = rentRepository;
        this.reservationRepository = reservationRepository;
    }

    RentModel save(RentDTO rentDTO) {
        throwExceptionIfRentWithReservationIdAlreadyExist(rentDTO.reservationId());
        RentModel rentToSave = createRentModelObjectBaseOnRentDTO(rentDTO);
        return rentRepository.save(rentToSave);
    }

    private RentModel createRentModelObjectBaseOnRentDTO(RentDTO rentModel) {
        RentModel rentToSave = new RentModel();
        rentToSave.setEmployee(rentModel.employee());
        rentToSave.setRentDate(rentModel.rentDate());
        rentToSave.setComments(rentModel.comments());
        ReservationModel reservationFromRepository = findReservationById(rentModel.reservationId());
        rentToSave.setReservation(reservationFromRepository);
        return rentToSave;
    }

    private ReservationModel findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Reservation with id " +
                        reservationId + " not found"));
    }

    private void throwExceptionIfRentWithReservationIdAlreadyExist(Long reservationId) {
                        List<Object[]> reservationIds = rentRepository.findRentWithReservationId(reservationId);

                if (!reservationIds.isEmpty()) {
            throw new RentAlreadyExistsForReservation("Rent already exist for reservation with id " + reservationId);
        }
    }
}