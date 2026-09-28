package com.paweljurkiewicz.carrental.reservation;

import com.paweljurkiewicz.carrental.car_rental_facility.BranchesModel;
import com.paweljurkiewicz.carrental.car_rental_facility.BranchesRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ReservationServiceTest {

    @Mock
    private CarRepository carRepositoryMock;

    @Mock
    private BranchesRepository branchesRepositoryMock;

    @Mock
    private ReservationRepository reservationRepositoryMock;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void shouldSaveReservation() {
        ReservationDTO reservationDto = new ReservationDTO(
                "Ted",
                1L,
                LocalDate.of(2023, 11, 20),
                LocalDate.of(2023, 11, 22),
                1L,
                2L
        );

        BranchesModel startBranch = new BranchesModel(1L, "Warszawa");
        Mockito.when(branchesRepositoryMock.findById(1L)).thenReturn(Optional.of(startBranch));

        BranchesModel endBranch = new BranchesModel(2L, "Gdynia");
        Mockito.when(branchesRepositoryMock.findById(2L)).thenReturn(Optional.of(endBranch));

        CarModel car = new CarModel(
                1L,
                "KIA",
                "Ceed",
                "Sedan",
                CarStatus.AVAILABLE,
                BigDecimal.valueOf(100)
        );

        Mockito.when(carRepositoryMock.findById(1L)).thenReturn(Optional.of(car));
        Mockito.when(reservationRepositoryMock.existsOverlappingReservation(
                Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)
        )).thenReturn(false);

        reservationService.saveReservation(reservationDto);

        Mockito.verify(carRepositoryMock).findById(1L);

        ArgumentCaptor<ReservationModel> captor = ArgumentCaptor.forClass(ReservationModel.class);
        Mockito.verify(reservationRepositoryMock).save(captor.capture());
        ReservationModel result = captor.getValue();

        assertThat(result.getEndDate()).isEqualTo("2023-11-22");
        assertThat(result.getStartDate()).isEqualTo("2023-11-20");
        assertThat(result.getPrice().intValue()).isEqualTo(200);
        assertThat(result.getCar()).isEqualTo(car);
    }

    @Test
    void shouldThrowCarNotAvailableExceptionWhenCarIsAlreadyReserved() {
        ReservationDTO reservationDto = new ReservationDTO(
                "Ted",
                1L,
                LocalDate.of(2023, 11, 20),
                LocalDate.of(2023, 11, 22),
                1L,
                2L
        );

        Mockito.when(reservationRepositoryMock.existsOverlappingReservation(
                Mockito.eq(1L), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)
        )).thenReturn(true);

        assertThatThrownBy(() -> reservationService.saveReservation(reservationDto))
                .isInstanceOf(CarNotAvailableException.class)
                .hasMessageContaining("already reserved");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenStartDateIsAfterEndDate() {
        ReservationDTO invalidDto = new ReservationDTO(
                "Ted",
                1L,
                LocalDate.of(2023, 11, 25),
                LocalDate.of(2023, 11, 20),
                1L,
                2L
        );

        assertThatThrownBy(() -> reservationService.saveReservation(invalidDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Start date cannot be after end date");
    }
}
