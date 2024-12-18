package com.amrazik.tennisclub.service;

import com.amrazik.tennisclub.data.DTO.CourtCreateDTO;
import com.amrazik.tennisclub.data.DTO.CourtResponseDTO;
import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.repository.CourtRepository;
import com.amrazik.tennisclub.data.repository.SurfaceRepository;
import com.amrazik.tennisclub.mapper.CourtMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourtService {
    private final CourtRepository courtRepository;
    private final SurfaceRepository surfaceRepository;
    private final CourtMapper courtMapper = CourtMapper.INSTANCE;

    @Autowired
    public CourtService(CourtRepository courtRepository, SurfaceRepository surfaceRepository) {
        this.courtRepository = courtRepository;
        this.surfaceRepository = surfaceRepository;
    }

    public CourtResponseDTO createCourt(CourtCreateDTO courtCreateDTO) {
        Court court = courtMapper.mapFromDTO(courtCreateDTO, surfaceRepository);
        return courtMapper.mapToDTO(courtRepository.create(court));
    }

    @Transactional(readOnly = true)
    public CourtResponseDTO getCourt(Long id) {
        Court court = courtRepository.findById(id);
        if (court == null) {
            throw new EntityNotFoundException("Court with id " + id + " not found");
        }
        return courtMapper.mapToDTO(court);
    }

    @Transactional(readOnly = true)
    public List<CourtResponseDTO> getCourts() {
        return courtMapper.mapToDTOList(courtRepository.findAll());
    }

    public CourtResponseDTO updateCourt(CourtCreateDTO courtCreateDTO) {
        Court court = courtMapper.mapFromDTO(courtCreateDTO, surfaceRepository);
        return courtMapper.mapToDTO(courtRepository.update(court));
    }

    public void deleteCourt(Long id) {
        courtRepository.delete(id);
    }
}
