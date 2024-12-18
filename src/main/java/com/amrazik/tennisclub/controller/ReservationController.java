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

/**
 * REST controller for managing reservations.
 * Provides endpoints for creating, retrieving, updating, and deleting reservations,
 * as well as filtering reservations by court or phone number.
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Constructs a ReservationController with the required {@link ReservationService}.
     *
     * @param reservationService The service layer handling reservation-related operations.
     */
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id The ID of the reservation to retrieve.
     * @return A {@link ResponseEntity} containing the reservation details in {@link ReservationResponseDTO}
     * if found, or a BAD_REQUEST status if an exception occurs.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(reservationService.getReservation(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves a list of all reservations.
     *
     * @return A {@link ResponseEntity} containing a list of {@link ReservationResponseDTO}.
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getReservations() {
        return new ResponseEntity<>(reservationService.getReservations(), HttpStatus.OK);
    }

    /**
     * Creates a new reservation.
     *
     * @param reservationCreateDTO The data transfer object containing reservation creation details.
     * @return A {@link ResponseEntity} containing the total cost of the reservation as {@link BigDecimal}
     * with a CREATED status, or a BAD_REQUEST status if an exception occurs.
     */
    @PostMapping
    public ResponseEntity<BigDecimal> createReservation(@RequestBody ReservationCreateDTO reservationCreateDTO) {
        try {
            return new ResponseEntity<>(reservationService.createReservation(reservationCreateDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates an existing reservation.
     *
     * @param reservationCreateDTO The data transfer object containing updated reservation details.
     * @return A {@link ResponseEntity} containing the updated reservation details in {@link ReservationResponseDTO}
     * with an OK status, or a BAD_REQUEST status if an exception occurs.
     */
    @PutMapping
    public ResponseEntity<ReservationResponseDTO> updateReservation(@RequestBody ReservationCreateDTO reservationCreateDTO) {
        try {
            return new ResponseEntity<>(reservationService.updateReservation(reservationCreateDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes a reservation by its ID.
     *
     * @param id The ID of the reservation to delete.
     * @return A {@link ResponseEntity} with an OK status if deletion is successful,
     * or a BAD_REQUEST status if an exception occurs.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves a list of reservations for a specific court.
     *
     * @param courtId The ID of the court.
     * @return A {@link ResponseEntity} containing a list of {@link ReservationResponseDTO}.
     */
    @GetMapping("/court/{courtId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByCourt(@PathVariable Long courtId) {
        try {
            List<ReservationResponseDTO> reservations = reservationService.getReservationsByCourt(courtId);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves a list of reservations based on a phone number.
     * Optionally filters for future reservations only.
     *
     * @param phoneNumber The phone number associated with the reservations.
     * @param future      Whether to filter for future reservations only (default: false).
     * @return A {@link ResponseEntity} containing a list of {@link ReservationResponseDTO}.
     */
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
