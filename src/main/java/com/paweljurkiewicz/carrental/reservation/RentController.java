package com.paweljurkiewicz.carrental.reservation;

import com.paweljurkiewicz.carrental.car_rental_facility.CarRentalModel;
import com.paweljurkiewicz.carrental.car_rental_facility.CarRentalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rents")
public class RentController {

    private final RentService service;

                        public RentController(RentService service) {
        this.service = service;
    }

    @PostMapping                     public RentModel save(@RequestBody @Valid RentDTO rent) {
        return service.save(rent);
    }
}
