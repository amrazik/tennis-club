package com.amrazik.tennisclub.controller;

import com.amrazik.tennisclub.data.DTO.CourtCreateDTO;
import com.amrazik.tennisclub.data.DTO.CourtResponseDTO;
import com.amrazik.tennisclub.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing tennis courts.
 * Provides endpoints for CRUD operations on courts.
 */
@RestController
@RequestMapping("/courts")
public class CourtController {

    private final CourtService courtService;

    /**
     * Constructs a CourtController with the required {@link CourtService}.
     *
     * @param courtService The service layer handling court-related operations.
     */
    @Autowired
    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    /**
     * Retrieves a court by its ID.
     *
     * @param id The ID of the court to retrieve.
     * @return A {@link ResponseEntity} containing the court details in {@link CourtResponseDTO}
     * if found, or a BAD_REQUEST status if an exception occurs.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourtResponseDTO> getCourt(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(courtService.getCourt(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves a list of all courts.
     *
     * @return A {@link ResponseEntity} containing a list of {@link CourtResponseDTO}.
     */
    @GetMapping
    public ResponseEntity<List<CourtResponseDTO>> getCourts() {
        return new ResponseEntity<>(courtService.getCourts(), HttpStatus.OK);
    }

    /**
     * Creates a new court.
     *
     * @param courtCreateDTO The data transfer object containing court creation details.
     * @return A {@link ResponseEntity} containing the created court details in {@link CourtResponseDTO}
     * with a CREATED status, or a BAD_REQUEST status if an exception occurs.
     */
    @PostMapping
    public ResponseEntity<CourtResponseDTO> createCourt(@RequestBody CourtCreateDTO courtCreateDTO) {
        try {
            return new ResponseEntity<>(courtService.createCourt(courtCreateDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates an existing court.
     *
     * @param courtCreateDTO The data transfer object containing updated court details.
     * @return A {@link ResponseEntity} containing the updated court details in {@link CourtResponseDTO}
     * with an OK status, or a BAD_REQUEST status if an exception occurs.
     */
    @PutMapping
    public ResponseEntity<CourtResponseDTO> updateCourt(@RequestBody CourtCreateDTO courtCreateDTO) {
        try {
            return new ResponseEntity<>(courtService.updateCourt(courtCreateDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes a court by its ID.
     *
     * @param id The ID of the court to delete.
     * @return A {@link ResponseEntity} with an OK status if deletion is successful,
     * or a BAD_REQUEST status if an exception occurs.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        try {
            courtService.deleteCourt(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
