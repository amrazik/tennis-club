package com.amrazik.tennisclub.controller;

import com.amrazik.tennisclub.data.DTO.ReservationCreateDTO;
import com.amrazik.tennisclub.data.DTO.ReservationResponseDTO;
import com.amrazik.tennisclub.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(reservationService.getReservation(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getReservations() {
        return new ResponseEntity<>(reservationService.getReservations(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BigDecimal> createReservation(@RequestBody ReservationCreateDTO reservationCreateDTO) {
        try {
            return new ResponseEntity<>(reservationService.createReservation(reservationCreateDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<ReservationResponseDTO> updateReservation(@RequestBody ReservationCreateDTO reservationCreateDTO) {
        try {
            return new ResponseEntity<>(reservationService.updateReservation(reservationCreateDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/court/{courtId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByCourt(@PathVariable Long courtId) {
        try {
            List<ReservationResponseDTO> reservations = reservationService.getReservationsByCourt(courtId);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByPhoneNumber(
            @PathVariable String phoneNumber,
            @RequestParam(value = "future", required = false, defaultValue = "false") boolean future) {
        try {
            List<ReservationResponseDTO> reservations = reservationService.getReservationsByPhoneNumber(phoneNumber, future);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
