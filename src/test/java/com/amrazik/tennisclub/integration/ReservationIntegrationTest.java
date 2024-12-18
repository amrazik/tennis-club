package com.amrazik.tennisclub.integration;

import com.amrazik.tennisclub.controller.ReservationController;
import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.Reservation;
import com.amrazik.tennisclub.data.model.Surface;
import com.amrazik.tennisclub.data.model.User;
import com.amrazik.tennisclub.data.repository.CourtRepository;
import com.amrazik.tennisclub.data.repository.ReservationRepository;
import com.amrazik.tennisclub.data.repository.SurfaceRepository;
import com.amrazik.tennisclub.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class ReservationIntegrationTest {

    @Autowired
    private SurfaceRepository surfaceRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private MockMvc mockMvc;

    @Autowired
    private ReservationController reservationController;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    @Test
    void testCreateReservation_Success() throws Exception {
        // Arrange
        Surface surface = new Surface();
        surface.setName("Clay");
        surface.setPricePerMinute(BigDecimal.valueOf(1.0));
        Surface savedSurface = surfaceRepository.create(surface);

        Court court = new Court();
        court.setName("Court 1");
        court.setSurface(savedSurface);
        Court createdCourt = courtRepository.create(court);

        Long courtId = createdCourt.getId();
        String userName = "John Doe";
        String phoneNumber = "123456789";
        String startTime = "2024-10-10T10:00";
        String endTime = "2024-10-10T11:00";
        boolean isDoubles = true;
        String reservationCreateJson = "{\"courtId\": " + courtId
                + ", \"userName\": \"" + userName
                + "\", \"phoneNumber\": \"" + phoneNumber
                + "\", \"startTime\": \"" + startTime
                + "\", \"endTime\": \"" + endTime
                + "\", \"isDoubles\": " + isDoubles
                + "}";

        // Act & Assert
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationCreateJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(60));
    }

    @Test
    void testGetReservationById_Success() throws Exception {
        // Arrange
        Surface surface = new Surface();
        surface.setName("Clay");
        surface.setPricePerMinute(BigDecimal.valueOf(1.0));
        Surface savedSurface = surfaceRepository.create(surface);

        Court court = new Court();
        court.setName("Court 1");
        court.setSurface(savedSurface);
        Court createdCourt = courtRepository.create(court);

        User user = new User();
        user.setName("John Doe");
        user.setPhoneNumber("123456789");
        User createdUser = userRepository.create(user);

        Reservation reservation = new Reservation();
        reservation.setCourt(createdCourt);
        reservation.setUser(createdUser);
        reservation.setStartTime(LocalDateTime.parse("2024-11-11T09:00"));
        reservation.setEndTime(LocalDateTime.parse("2024-11-11T10:00"));
        reservation.setDoubles(true);
        reservation.setTotalPrice(BigDecimal.valueOf(60));
        Reservation createdReservation = reservationRepository.create(reservation);

        // Act & Assert
        mockMvc.perform(get("/reservations/" + createdReservation.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.court.name").value("Court 1"))
                .andExpect(jsonPath("$.court.surface.name").value("Clay"))
                .andExpect(jsonPath("$.user.name").value("John Doe"))
                .andExpect(jsonPath("$.user.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.startTime").isArray())
                .andExpect(jsonPath("$.endTime").isArray())
                .andExpect(jsonPath("$.doubles").value(true))
                .andExpect(jsonPath("$.totalPrice").value(60));
    }

    @Test
    void testUpdateReservation_Success() throws Exception {
        // Arrange
        Surface surface = new Surface();
        surface.setName("Clay");
        surface.setPricePerMinute(BigDecimal.valueOf(1.0));
        Surface savedSurface = surfaceRepository.create(surface);

        Court court = new Court();
        court.setName("Court 1");
        court.setSurface(savedSurface);
        Court createdCourt = courtRepository.create(court);

        User user = new User();
        user.setName("John Doe");
        user.setPhoneNumber("123456789");
        User createdUser = userRepository.create(user);

        Reservation reservation = new Reservation();
        reservation.setCourt(createdCourt);
        reservation.setUser(createdUser);
        reservation.setStartTime(LocalDateTime.parse("2024-12-12T09:00"));
        reservation.setEndTime(LocalDateTime.parse("2024-12-12T10:00"));
        reservation.setDoubles(true);
        reservation.setTotalPrice(BigDecimal.valueOf(60));
        Reservation createdReservation = reservationRepository.create(reservation);

        Long reservationId = createdReservation.getId();
        Long courtId = createdCourt.getId();
        String userName = "John Doe";
        String phoneNumber = "123456789";
        String startTime = "2024-12-12T11:00";
        String endTime = "2024-12-12T13:00";
        boolean isDoubles = true;
        String reservationCreateJson = "{\"id\": " + reservationId
                + ", \"courtId\": " + courtId
                + ", \"userName\": \"" + userName
                + "\", \"phoneNumber\": \"" + phoneNumber
                + "\", \"startTime\": \"" + startTime
                + "\", \"endTime\": \"" + endTime
                + "\", \"isDoubles\": " + isDoubles
                + "}";

        // Act & Assert
        mockMvc.perform(put("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationCreateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.court.name").value("Court 1"))
                .andExpect(jsonPath("$.court.surface.name").value("Clay"))
                .andExpect(jsonPath("$.user.name").value("John Doe"))
                .andExpect(jsonPath("$.user.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.startTime").isArray())
                .andExpect(jsonPath("$.endTime").isArray())
                .andExpect(jsonPath("$.doubles").value(true))
                .andExpect(jsonPath("$.totalPrice").value(120));
    }

    @Test
    void testDeleteReservation_Success() throws Exception {
        // Arrange
        Surface surface = new Surface();
        surface.setName("Clay");
        surface.setPricePerMinute(BigDecimal.valueOf(1.0));
        Surface savedSurface = surfaceRepository.create(surface);

        Court court = new Court();
        court.setName("Court 1");
        court.setSurface(savedSurface);
        Court createdCourt = courtRepository.create(court);

        User user = new User();
        user.setName("John Doe");
        user.setPhoneNumber("123456789");
        User createdUser = userRepository.create(user);

        Reservation reservation = new Reservation();
        reservation.setCourt(createdCourt);
        reservation.setUser(createdUser);
        reservation.setStartTime(LocalDateTime.parse("2024-09-08T09:00"));
        reservation.setEndTime(LocalDateTime.parse("2024-09-08T10:00"));
        reservation.setDoubles(true);
        reservation.setTotalPrice(BigDecimal.valueOf(60));
        Reservation createdReservation = reservationRepository.create(reservation);

        // Act & Assert
        mockMvc.perform(delete("/reservations/" + createdReservation.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Reservation deletedReservation = reservationRepository.findById(createdReservation.getId());
        assert (deletedReservation.isDeleted());
    }
}
