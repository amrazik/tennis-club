package com.amrazik.tennisclub.controller;

import com.amrazik.tennisclub.data.DTO.CourtCreateDTO;
import com.amrazik.tennisclub.data.DTO.CourtResponseDTO;
import com.amrazik.tennisclub.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courts")
public class CourtController {

    private final CourtService courtService;

    @Autowired
    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourtResponseDTO> getCourt(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(courtService.getCourt(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<CourtResponseDTO>> getCourts() {
        return new ResponseEntity<>(courtService.getCourts(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CourtResponseDTO> createCourt(@RequestBody CourtCreateDTO courtCreateDTO) {
        try {
            return new ResponseEntity<>(courtService.createCourt(courtCreateDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<CourtResponseDTO> updateCourt(@RequestBody CourtCreateDTO courtCreateDTO) {
        try {
            return new ResponseEntity<>(courtService.updateCourt(courtCreateDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
