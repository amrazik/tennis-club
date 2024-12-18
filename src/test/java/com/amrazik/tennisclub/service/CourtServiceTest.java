package com.amrazik.tennisclub.service;

import com.amrazik.tennisclub.data.DTO.CourtCreateDTO;
import com.amrazik.tennisclub.data.DTO.CourtResponseDTO;
import com.amrazik.tennisclub.data.DTO.SurfaceDTO;
import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.Surface;
import com.amrazik.tennisclub.data.repository.CourtRepository;
import com.amrazik.tennisclub.data.repository.SurfaceRepository;
import com.amrazik.tennisclub.mapper.CourtMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class CourtServiceTest {

    @Mock
    private CourtRepository courtRepository;

    @Mock
    private SurfaceRepository surfaceRepository;

    @Mock
    private CourtMapper courtMapper;

    @InjectMocks
    private CourtService courtService;

    @Test
    void testCreateCourt() {
        Long surfaceId = 1L;
        String courtName = "Court A";
        CourtCreateDTO courtCreateDTO = new CourtCreateDTO();
        courtCreateDTO.setName(courtName);
        courtCreateDTO.setSurfaceId(surfaceId);

        Surface surface = new Surface();
        surface.setId(surfaceId);
        surface.setName("Surface A");
        surface.setPricePerMinute(new BigDecimal("100.00"));

        Court court = new Court();
        court.setName(courtName);
        court.setSurface(surface);

        CourtResponseDTO courtResponseDTO = new CourtResponseDTO();
        courtResponseDTO.setId(court.getId());
        courtResponseDTO.setName(court.getName());

        SurfaceDTO surfaceDTO = new SurfaceDTO();
        surfaceDTO.setId(surface.getId());
        surfaceDTO.setName(surface.getName());
        courtResponseDTO.setSurface(surfaceDTO);

        when(surfaceRepository.findById(surfaceId)).thenReturn(surface);
        when(courtMapper.mapFromDTO(courtCreateDTO, surfaceRepository)).thenReturn(court);
        when(courtRepository.create(any(Court.class))).thenReturn(court);
        when(courtMapper.mapToDTO(court)).thenReturn(courtResponseDTO);

        CourtResponseDTO result = courtService.createCourt(courtCreateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(courtName);
        assertThat(result.getSurface().getId()).isEqualTo(surfaceId);
        verify(courtRepository, times(1)).create(any(Court.class));
    }

    @Test
    void testGetCourt() {
        Long courtId = 1L;
        Court court = new Court();
        court.setId(courtId);
        court.setName("Court A");

        Surface surface = new Surface();
        surface.setId(1L);
        surface.setName("Surface A");
        court.setSurface(surface);

        CourtResponseDTO courtResponseDTO = new CourtResponseDTO();
        courtResponseDTO.setId(courtId);
        courtResponseDTO.setName("Court A");

        SurfaceDTO surfaceDTO = new SurfaceDTO();
        surfaceDTO.setId(surface.getId());
        surfaceDTO.setName(surface.getName());
        courtResponseDTO.setSurface(surfaceDTO);

        when(courtRepository.findById(courtId)).thenReturn(court);
        when(courtMapper.mapToDTO(court)).thenReturn(courtResponseDTO);

        CourtResponseDTO result = courtService.getCourt(courtId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(courtId);
        assertThat(result.getSurface().getId()).isEqualTo(surface.getId());
        verify(courtRepository, times(1)).findById(courtId);
    }

    @Test
    void testGetCourt_NotFound() {
        Long courtId = 999L;
        when(courtRepository.findById(courtId)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> courtService.getCourt(courtId));

        assertThat(exception.getMessage()).contains("Court with id " + courtId + " not found");
    }

    @Test
    void testGetCourts() {
        Court court1 = new Court();
        court1.setId(1L);
        court1.setName("Court A");

        Surface surface1 = new Surface();
        surface1.setId(1L);
        surface1.setName("Surface A");
        court1.setSurface(surface1);

        Court court2 = new Court();
        court2.setId(2L);
        court2.setName("Court B");

        Surface surface2 = new Surface();
        surface2.setId(2L);
        surface2.setName("Surface B");
        court2.setSurface(surface2);

        List<Court> courts = List.of(court1, court2);

        CourtResponseDTO courtResponseDTO1 = new CourtResponseDTO();
        courtResponseDTO1.setId(court1.getId());
        courtResponseDTO1.setName(court1.getName());

        SurfaceDTO surfaceDTO1 = new SurfaceDTO();
        surfaceDTO1.setId(surface1.getId());
        surfaceDTO1.setName(surface1.getName());
        courtResponseDTO1.setSurface(surfaceDTO1);

        CourtResponseDTO courtResponseDTO2 = new CourtResponseDTO();
        courtResponseDTO2.setId(court2.getId());
        courtResponseDTO2.setName(court2.getName());

        SurfaceDTO surfaceDTO2 = new SurfaceDTO();
        surfaceDTO2.setId(surface2.getId());
        surfaceDTO2.setName(surface2.getName());
        courtResponseDTO2.setSurface(surfaceDTO2);

        when(courtRepository.findAll()).thenReturn(courts);
        when(courtMapper.mapToDTOList(courts)).thenReturn(List.of(courtResponseDTO1, courtResponseDTO2));

        List<CourtResponseDTO> result = courtService.getCourts();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Court A");
        assertThat(result.get(1).getName()).isEqualTo("Court B");
        verify(courtRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCourt() {
        // Arrange
        Long surfaceId = 1L;
        Surface surface = new Surface();
        surface.setId(surfaceId);
        surface.setName("Updated Surface");
        surface.setPricePerMinute(new BigDecimal("120.00"));

        long courtId = 1L;
        CourtCreateDTO courtCreateDTO = new CourtCreateDTO();
        courtCreateDTO.setId(1L);
        courtCreateDTO.setName("Court A Updated");
        courtCreateDTO.setSurfaceId(surfaceId);

        Court court = new Court();
        court.setId(courtId);
        court.setName("Court A Updated");
        court.setSurface(surface);

        SurfaceDTO surfaceDTO = new SurfaceDTO();
        surfaceDTO.setId(surface.getId());
        surfaceDTO.setName(surface.getName());

        CourtResponseDTO courtResponseDTO = new CourtResponseDTO();
        courtResponseDTO.setId(court.getId());
        courtResponseDTO.setName(court.getName());
        courtResponseDTO.setSurface(surfaceDTO);

        when(surfaceRepository.findById(surfaceId)).thenReturn(surface);
        when(courtMapper.mapFromDTO(courtCreateDTO, surfaceRepository)).thenReturn(court);
        when(courtRepository.update(any(Court.class))).thenReturn(court);
        when(courtMapper.mapToDTO(court)).thenReturn(courtResponseDTO);

        CourtResponseDTO result = courtService.updateCourt(courtCreateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(courtCreateDTO.getName());
        assertThat(result.getSurface().getId()).isEqualTo(courtCreateDTO.getSurfaceId());
        verify(courtRepository, times(1)).update(any(Court.class));
    }

    @Test
    void testDeleteCourt() {
        Long courtId = 1L;
        courtService.deleteCourt(courtId);
        verify(courtRepository, times(1)).delete(courtId);
    }
}
