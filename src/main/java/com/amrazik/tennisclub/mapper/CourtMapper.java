package com.amrazik.tennisclub.mapper;

import com.amrazik.tennisclub.data.DTO.CourtCreateDTO;
import com.amrazik.tennisclub.data.DTO.CourtResponseDTO;
import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.Surface;
import com.amrazik.tennisclub.data.repository.SurfaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper interface for converting between {@link Court} entities and their corresponding DTOs.
 *
 * This interface defines methods for mapping {@link Court} entities to {@link CourtResponseDTO} objects
 * and vice versa. It utilizes the MapStruct framework to automatically generate the implementation
 * for the defined mapping methods. The {@link CourtMapper} also includes a default method for converting
 * {@link CourtCreateDTO} to {@link Court}, with additional logic to fetch the related {@link Surface}
 * entity from the {@link SurfaceRepository}.
 */
@Mapper
public interface CourtMapper {

    /**
     * An instance of {@link CourtMapper} for use in the application.
     */
    CourtMapper INSTANCE = Mappers.getMapper(CourtMapper.class);

    /**
     * Maps a {@link Court} entity to a {@link CourtResponseDTO}.
     *
     * @param court The {@link Court} entity to be mapped.
     * @return The mapped {@link CourtResponseDTO} object.
     */
    CourtResponseDTO mapToDTO(Court court);

    /**
     * Maps a {@link CourtCreateDTO} to a {@link Court} entity.
     *
     * This method also retrieves the associated {@link Surface} entity using its ID from the
     * {@link SurfaceRepository}. If no {@link Surface} is found with the given ID, an exception
     * will be thrown.
     *
     * @param courtCreateDTO The {@link CourtCreateDTO} containing the data to be mapped.
     * @param surfaceRepository The repository used to retrieve the {@link Surface} entity.
     * @return The mapped {@link Court} entity.
     * @throws IllegalArgumentException If the {@link CourtCreateDTO} does not provide a valid surfaceId.
     * @throws EntityNotFoundException If no {@link Surface} entity is found for the provided surfaceId.
     */
    default Court mapFromDTO(CourtCreateDTO courtCreateDTO, SurfaceRepository surfaceRepository) {
        Long surfaceId = courtCreateDTO.getSurfaceId();
        if (surfaceId == null) {
            throw new IllegalArgumentException("Missing surfaceId");
        }
        System.out.println("Surface id: " + surfaceId);
        Surface surface = surfaceRepository.findById(surfaceId);
        System.out.println("Surface: " + surface);
        if (surface == null) {
            System.out.println("Surface with id " + surfaceId + " not found");
            throw new EntityNotFoundException("Surface with id " + surfaceId + " not found");
        }
        Court court = new Court();
        court.setId(courtCreateDTO.getId());
        court.setName(courtCreateDTO.getName());
        court.setSurface(surface);
        return court;
    }

    /**
     * Maps a list of {@link Court} entities to a list of {@link CourtResponseDTO} objects.
     *
     * @param courts The list of {@link Court} entities to be mapped.
     * @return The list of mapped {@link CourtResponseDTO} objects.
     */
    List<CourtResponseDTO> mapToDTOList(List<Court> courts);
}
