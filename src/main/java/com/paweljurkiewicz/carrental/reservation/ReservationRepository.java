package com.paweljurkiewicz.carrental.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ReservationRepository extends JpaRepository<ReservationModel, Long> {

    @Query("""
        SELECT COUNT(r) > 0 FROM ReservationModel r
        WHERE r.car.id = :carId
          AND r.startDate < :endDate
          AND r.endDate > :startDate
    """)
    boolean existsOverlappingReservation(
        @Param("carId") Long carId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
