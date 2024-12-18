package com.amrazik.tennisclub.service;

import com.amrazik.tennisclub.data.DTO.ReservationCreateDTO;
import com.amrazik.tennisclub.data.DTO.ReservationResponseDTO;
import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.Reservation;
import com.amrazik.tennisclub.data.model.Surface;
import com.amrazik.tennisclub.data.model.User;
import com.amrazik.tennisclub.data.repository.CourtRepository;
import com.amrazik.tennisclub.data.repository.ReservationRepository;
import com.amrazik.tennisclub.data.repository.SurfaceRepository;
import com.amrazik.tennisclub.data.repository.UserRepository;
import com.amrazik.tennisclub.mapper.ReservationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CourtRepository courtRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private SurfaceRepository surfaceRepository;

    @InjectMocks
    private ReservationService reservationService;


    @Test
    void testCreateReservation() {
        // Arrange
        Surface surface = new Surface();
        surface.setId(1L);
        surface.setName("Indoor Court");
        surface.setPricePerMinute(new BigDecimal("100"));

        Court court = new Court();
        court.setId(1L);
        court.setName("Court A");
        court.setSurface(surface);

        Long reservationId = 1L; // This ID will be assigned when saving
        String name = "John Doe";
        String phoneNumber = "123456789";
        ReservationCreateDTO reservationCreateDTO = new ReservationCreateDTO();
        reservationCreateDTO.setCourtId(court.getId());
        reservationCreateDTO.setUserName(name);
        reservationCreateDTO.setPhoneNumber(phoneNumber);
        reservationCreateDTO.setStartTime(LocalDateTime.now());
        reservationCreateDTO.setEndTime(LocalDateTime.now().plusMinutes(30));
        reservationCreateDTO.setDoubles(true);

        User user = new User();
        user.setId(1L);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);

        Reservation reservation = new Reservation();
        reservation.setId(reservationId); // This ID is generated during saving
        reservation.setUser(user);
        reservation.setCourt(court);
        reservation.setStartTime(reservationCreateDTO.getStartTime());
        reservation.setEndTime(reservationCreateDTO.getEndTime());
        reservation.setDoubles(reservationCreateDTO.isDoubles());
        reservation.setTotalPrice(new BigDecimal("3000"));

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservationId); // Ensure the ID is set
        reservationResponseDTO.setUser(reservation.getUser());
        reservationResponseDTO.setCourt(reservation.getCourt());
        reservationResponseDTO.setStartTime(reservation.getStartTime());
        reservationResponseDTO.setEndTime(reservation.getEndTime());
        reservationResponseDTO.setDoubles(reservation.isDoubles());
        reservationResponseDTO.setTotalPrice(reservation.getTotalPrice());

        when(surfaceRepository.findById(surface.getId())).thenReturn(surface);
        when(courtRepository.findById(court.getId())).thenReturn(court);
        when(reservationMapper.mapFromDTO(reservationCreateDTO, courtRepository)).thenReturn(reservation);
        when(reservationRepository.create(any(Reservation.class))).thenReturn(reservation); // Save method for creation
        when(reservationMapper.mapToDTO(reservation)).thenReturn(reservationResponseDTO);

        // Act
        BigDecimal result = reservationService.createReservation(reservationCreateDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(new BigDecimal("3000"));

        verify(reservationRepository, times(1)).create(any(Reservation.class)); // Verify save method is called
    }

    @Test
    void testGetReservation() {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setTotalPrice(new BigDecimal("1000"));

        Court court = new Court();
        reservation.setCourt(court);
        User user = new User();
        reservation.setUser(user);

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservationId);
        reservationResponseDTO.setTotalPrice(new BigDecimal("1000"));

        when(reservationRepository.findById(reservationId)).thenReturn(reservation);
        when(reservationMapper.mapToDTO(reservation)).thenReturn(reservationResponseDTO);

        // Act
        ReservationResponseDTO result = reservationService.getReservation(reservationId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(reservationId);
        assertThat(result.getTotalPrice()).isEqualTo(new BigDecimal("1000"));
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testGetReservation_NotFound() {
        // Arrange
        Long reservationId = 999L;
        when(reservationRepository.findById(reservationId)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                reservationService.getReservation(reservationId));

        assertThat(exception.getMessage()).contains("Reservation with id " + reservationId + " not found");
    }

    @Test
    void testGetReservations() {
        // Arrange
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setTotalPrice(new BigDecimal("1500"));

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setTotalPrice(new BigDecimal("2000"));

        List<Reservation> reservations = List.of(reservation1, reservation2);

        ReservationResponseDTO reservationResponseDTO1 = new ReservationResponseDTO();
        reservationResponseDTO1.setId(1L);
        reservationResponseDTO1.setTotalPrice(new BigDecimal("1500"));

        ReservationResponseDTO reservationResponseDTO2 = new ReservationResponseDTO();
        reservationResponseDTO2.setId(2L);
        reservationResponseDTO2.setTotalPrice(new BigDecimal("2000"));

        when(reservationRepository.findAll()).thenReturn(reservations);
        when(reservationMapper.mapToDTOList(reservations)).thenReturn(List.of(reservationResponseDTO1, reservationResponseDTO2));

        // Act
        List<ReservationResponseDTO> result = reservationService.getReservations();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTotalPrice()).isEqualTo(new BigDecimal("1500"));
        assertThat(result.get(1).getTotalPrice()).isEqualTo(new BigDecimal("2000"));
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testUpdateReservation() {
        // Arrange
        Surface surface = new Surface();
        surface.setId(1L);
        surface.setName("Indoor Court");
        surface.setPricePerMinute(new BigDecimal("100"));

        Court court = new Court();
        court.setId(1L);
        court.setName("Court A");
        court.setSurface(surface);

        Long reservationId = 1L;
        String name = "John Doe";
        String phoneNumber = "123456789";
        ReservationCreateDTO reservationCreateDTO = new ReservationCreateDTO();
        reservationCreateDTO.setId(1L);
        reservationCreateDTO.setCourtId(court.getId());
        reservationCreateDTO.setUserName(name);
        reservationCreateDTO.setPhoneNumber(phoneNumber);
        reservationCreateDTO.setStartTime(LocalDateTime.now());
        reservationCreateDTO.setEndTime(LocalDateTime.now().plusMinutes(30));
        reservationCreateDTO.setDoubles(true);

        User user = new User();
        user.setId(1L);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);

        Reservation reservation = new Reservation();
        reservation.setId(reservationId); // Ensure the ID is set
        reservation.setUser(user);
        reservation.setCourt(court);
        reservation.setStartTime(reservationCreateDTO.getStartTime());
        reservation.setEndTime(reservationCreateDTO.getEndTime());
        reservation.setDoubles(reservationCreateDTO.isDoubles());
        reservation.setTotalPrice(new BigDecimal("3000"));

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservationId); // Ensure the ID is set
        reservationResponseDTO.setUser(reservation.getUser());
        reservationResponseDTO.setCourt(reservation.getCourt());
        reservationResponseDTO.setStartTime(reservation.getStartTime());
        reservationResponseDTO.setEndTime(reservation.getEndTime());
        reservationResponseDTO.setDoubles(reservation.isDoubles());
        reservationResponseDTO.setTotalPrice(reservation.getTotalPrice());

        when(surfaceRepository.findById(surface.getId())).thenReturn(surface);
        when(courtRepository.findById(court.getId())).thenReturn(court);
        when(reservationMapper.mapFromDTO(reservationCreateDTO, courtRepository)).thenReturn(reservation);
        when(reservationRepository.update(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.mapToDTO(reservation)).thenReturn(reservationResponseDTO);

        // Act
        ReservationResponseDTO result = reservationService.updateReservation(reservationCreateDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(reservationId);
        assertThat(result.getCourt().getId()).isEqualTo(court.getId());
        assertThat(result.getUser().getId()).isEqualTo(user.getId());
        assertThat(result.getStartTime()).isEqualTo(reservationCreateDTO.getStartTime());
        assertThat(result.getEndTime()).isEqualTo(reservationCreateDTO.getEndTime());
        assertThat(result.isDoubles()).isEqualTo(reservationCreateDTO.isDoubles());
        assertThat(result.getTotalPrice()).isEqualTo(new BigDecimal("3000"));

        verify(reservationRepository, times(1)).update(any(Reservation.class));
    }

    @Test
    void testDeleteReservation() {
        // Arrange
        Long reservationId = 1L;

        // Act
        reservationService.deleteReservation(reservationId);

        // Assert
        verify(reservationRepository, times(1)).delete(reservationId);
    }

    @Test
    void testGetReservationsByCourt() {
        // Arrange
        Long courtId = 1L;
        Reservation reservation = new Reservation();
        reservation.setCourt(new Court());
        reservation.getCourt().setId(courtId);

        List<Reservation> reservations = List.of(reservation);

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setCourt(new Court());
        reservationResponseDTO.getCourt().setId(courtId);

        when(reservationRepository.findByCourtId(courtId)).thenReturn(reservations);
        when(reservationMapper.mapToDTOList(reservations)).thenReturn(List.of(reservationResponseDTO));

        // Act
        List<ReservationResponseDTO> result = reservationService.getReservationsByCourt(courtId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourt().getId()).isEqualTo(courtId);
        verify(reservationRepository, times(1)).findByCourtId(courtId);
    }

    @Test
    void testGetReservationsByPhoneNumber() {
        // Arrange
        String phoneNumber = "123456789";
        Reservation reservation = new Reservation();
        reservation.setUser(new User());
        reservation.getUser().setPhoneNumber(phoneNumber);

        List<Reservation> reservations = List.of(reservation);

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setUser(new User());
        reservationResponseDTO.getUser().setPhoneNumber(phoneNumber);

        when(reservationRepository.findByPhoneNumber(phoneNumber, true)).thenReturn(reservations);
        when(reservationMapper.mapToDTOList(reservations)).thenReturn(List.of(reservationResponseDTO));

        // Act
        List<ReservationResponseDTO> result = reservationService.getReservationsByPhoneNumber(phoneNumber, true);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUser().getPhoneNumber()).isEqualTo(phoneNumber);
        verify(reservationRepository, times(1)).findByPhoneNumber(phoneNumber, true);
    }
}
