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

@Mapper
public interface CourtMapper {
    CourtMapper INSTANCE = Mappers.getMapper(CourtMapper.class);

    CourtResponseDTO mapToDTO(Court court);

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

    List<CourtResponseDTO> mapToDTOList(List<Court> courts);

}
