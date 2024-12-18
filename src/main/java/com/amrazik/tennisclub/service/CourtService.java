package com.amrazik.tennisclub.service;

import com.amrazik.tennisclub.data.DTO.CourtCreateDTO;
import com.amrazik.tennisclub.data.DTO.CourtResponseDTO;
import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.Surface;
import com.amrazik.tennisclub.data.repository.CourtRepository;
import com.amrazik.tennisclub.data.repository.SurfaceRepository;
import com.amrazik.tennisclub.mapper.CourtMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class responsible for handling business logic related to {@link Court} entities.
 *
 * The {@link CourtService} class manages operations for creating, retrieving, updating, and deleting {@link Court}
 * entities. It interacts with the {@link CourtRepository} for data persistence and uses the {@link CourtMapper}
 * to map between {@link Court} entities and their corresponding DTOs. This service ensures that business logic
 * is applied correctly and that data integrity is maintained throughout the application. Methods are transactional
 * where appropriate, with read-only transactions for retrieval operations.
 */
@Service
@Transactional
public class CourtService {

    private final CourtRepository courtRepository;
    private final SurfaceRepository surfaceRepository;
    private final CourtMapper courtMapper = CourtMapper.INSTANCE;

    /**
     * Constructs a new {@link CourtService} with the provided repositories.
     *
     * @param courtRepository The {@link CourtRepository} used for persistence operations on {@link Court} entities.
     * @param surfaceRepository The {@link SurfaceRepository} used for retrieving {@link Surface} entities.
     */
    @Autowired
    public CourtService(CourtRepository courtRepository, SurfaceRepository surfaceRepository) {
        this.courtRepository = courtRepository;
        this.surfaceRepository = surfaceRepository;
    }

    /**
     * Creates a new {@link Court} based on the provided {@link CourtCreateDTO}.
     *
     * The method maps the DTO to a {@link Court} entity and persists it in the database using
     * the {@link CourtRepository}. It returns a {@link CourtResponseDTO} containing the details
     * of the newly created {@link Court}.
     *
     * @param courtCreateDTO The DTO containing the data for the new {@link Court}.
     * @return The DTO representation of the created {@link Court}.
     */
    public CourtResponseDTO createCourt(CourtCreateDTO courtCreateDTO) {
        Court court = courtMapper.mapFromDTO(courtCreateDTO, surfaceRepository);
        return courtMapper.mapToDTO(courtRepository.create(court));
    }

    /**
     * Retrieves a {@link Court} by its ID and returns it as a {@link CourtResponseDTO}.
     *
     * This method checks if the {@link Court} exists in the database. If not, an {@link EntityNotFoundException}
     * is thrown. It uses the {@link CourtMapper} to map the {@link Court} entity to a DTO.
     *
     * @param id The ID of the {@link Court} to retrieve.
     * @return The DTO representation of the {@link Court}.
     * @throws EntityNotFoundException If no {@link Court} with the specified ID is found.
     */
    @Transactional(readOnly = true)
    public CourtResponseDTO getCourt(Long id) {
        Court court = courtRepository.findById(id);
        if (court == null) {
            throw new EntityNotFoundException("Court with id " + id + " not found");
        }
        return courtMapper.mapToDTO(court);
    }

    /**
     * Retrieves all {@link Court}s and returns them as a list of {@link CourtResponseDTO}s.
     *
     * @return A list of DTO representations of all {@link Court}s.
     */
    @Transactional(readOnly = true)
    public List<CourtResponseDTO> getCourts() {
        return courtMapper.mapToDTOList(courtRepository.findAll());
    }

    /**
     * Updates an existing {@link Court} based on the provided {@link CourtCreateDTO}.
     *
     * The method maps the DTO to a {@link Court} entity, updates the entity in the database,
     * and returns the updated entity as a {@link CourtResponseDTO}.
     *
     * @param courtCreateDTO The DTO containing the updated data for the {@link Court}.
     * @return The DTO representation of the updated {@link Court}.
     */
    public CourtResponseDTO updateCourt(CourtCreateDTO courtCreateDTO) {
        Court court = courtMapper.mapFromDTO(courtCreateDTO, surfaceRepository);
        return courtMapper.mapToDTO(courtRepository.update(court));
    }

    /**
     * Deletes a {@link Court} by its ID.
     *
     * The method marks the {@link Court} as deleted by setting its deleted flag to true.
     *
     * @param id The ID of the {@link Court} to delete.
     */
    public void deleteCourt(Long id) {
        courtRepository.delete(id);
    }
}
