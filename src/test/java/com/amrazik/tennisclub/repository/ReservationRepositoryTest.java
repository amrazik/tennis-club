package com.amrazik.tennisclub.repository;

import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.Reservation;
import com.amrazik.tennisclub.data.model.Surface;
import com.amrazik.tennisclub.data.model.User;
import com.amrazik.tennisclub.data.repository.CourtRepository;
import com.amrazik.tennisclub.data.repository.ReservationRepository;
import com.amrazik.tennisclub.data.repository.SurfaceRepository;
import com.amrazik.tennisclub.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;


    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SurfaceRepository surfaceRepository;

    @Test
    void testCreate() {
        // Create a Surface
        Surface surface = new Surface();
        surface.setName("Grass");
        surface.setPricePerMinute(BigDecimal.valueOf(1));
        surfaceRepository.create(surface);

        // Create a Court with a Surface
        Court court = new Court();
        court.setName("Court A");
        court.setSurface(surface);  // Associate surface with court
        courtRepository.create(court);

        // Create a User
        User user = new User();
        user.setPhoneNumber("123456789");
        userRepository.create(user);

        // Create a Reservation for the Court and User
        Reservation reservation = new Reservation();
        reservation.setCourt(court);
        reservation.setUser(user);
        reservation.setStartTime(LocalDateTime.now().plusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(2));
        reservation.setTotalPrice(BigDecimal.valueOf(100.0));

        Reservation savedReservation = reservationRepository.create(reservation);

        // Validate that the Reservation is saved correctly
        assertThat(savedReservation.getId()).isNotNull();
        assertThat(savedReservation.getCourt()).isEqualTo(court);
        assertThat(savedReservation.getUser()).isEqualTo(user);
    }

    @Test
    void testFindAll() {
        // Create a Surface
        Surface surface = new Surface();
        surface.setName("Clay");
        surface.setPricePerMinute(BigDecimal.valueOf(2));
        surfaceRepository.create(surface);

        // Create Courts
        Court court1 = new Court();
        court1.setName("Court B");
        court1.setSurface(surface);
        courtRepository.create(court1);

        Court court2 = new Court();
        court2.setName("Court C");
        court2.setSurface(surface);
        court2.setDeleted(true); // This court should be excluded
        courtRepository.create(court2);

        // Create Users
        User user1 = new User();
        user1.setPhoneNumber("987654321");
        user1.setDeleted(false);
        userRepository.create(user1);

        User user2 = new User();
        user2.setPhoneNumber("123456789");
        user2.setDeleted(true); // This user should be excluded
        userRepository.create(user2);

        // Create Reservations
        Reservation reservation1 = new Reservation();
        reservation1.setCourt(court1);
        reservation1.setUser(user1);
        reservation1.setStartTime(LocalDateTime.now().plusHours(1));
        reservation1.setEndTime(LocalDateTime.now().plusHours(2));
        reservationRepository.create(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setCourt(court2);
        reservation2.setUser(user2);
        reservation2.setStartTime(LocalDateTime.now().plusHours(1));
        reservation2.setEndTime(LocalDateTime.now().plusHours(2));
        reservationRepository.create(reservation2);

        // Fetch all reservations
        List<Reservation> reservations = reservationRepository.findAll();

        // Only reservation1 should be included, as reservation2 has deleted court and user
        assertThat(reservations).hasSize(1)
                .extracting(Reservation::getId)
                .containsExactly(reservation1.getId());
    }

    @Test
    void testFindByCourtId() {
        // Create a Surface
        Surface surface = new Surface();
        surface.setName("Hard");
        surface.setPricePerMinute(BigDecimal.valueOf(3));
        surfaceRepository.create(surface);

        // Create a Court with a Surface
        Court court = new Court();
        court.setName("Court D");
        court.setSurface(surface);
        courtRepository.create(court);

        // Create a User
        User user = new User();
        user.setPhoneNumber("1122334455");
        user.setDeleted(false);
        userRepository.create(user);

        // Create Reservations for the Court and User
        Reservation reservation1 = new Reservation();
        reservation1.setCourt(court);
        reservation1.setUser(user);
        reservation1.setStartTime(LocalDateTime.now().plusHours(1));
        reservation1.setEndTime(LocalDateTime.now().plusHours(2));
        reservationRepository.create(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setCourt(court);
        reservation2.setUser(user);
        reservation2.setStartTime(LocalDateTime.now().plusHours(3));
        reservation2.setEndTime(LocalDateTime.now().plusHours(4));
        reservationRepository.create(reservation2);

        // Fetch reservations for the court by ID
        List<Reservation> reservations = reservationRepository.findByCourtId(court.getId());

        // Both reservations should be returned, sorted by start time
        assertThat(reservations).hasSize(2)
                .extracting(Reservation::getId)
                .containsExactly(reservation1.getId(), reservation2.getId());
    }

    @Test
    void testFindByPhoneNumber() {
        // Create a Surface
        Surface surface = new Surface();
        surface.setName("Grass");
        surface.setPricePerMinute(BigDecimal.valueOf(4));
        surfaceRepository.create(surface);

        // Create a Court with a Surface
        Court court = new Court();
        court.setName("Court E");
        court.setSurface(surface);
        courtRepository.create(court);

        // Create a User
        User user = new User();
        user.setPhoneNumber("555123456");
        user.setDeleted(false);
        userRepository.create(user);

        // Create Reservations for the Court and User
        Reservation reservation1 = new Reservation();
        reservation1.setCourt(court);
        reservation1.setUser(user);
        reservation1.setStartTime(LocalDateTime.now().plusHours(1));
        reservation1.setEndTime(LocalDateTime.now().plusHours(2));
        reservationRepository.create(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setCourt(court);
        reservation2.setUser(user);
        reservation2.setStartTime(LocalDateTime.now().plusHours(3));
        reservation2.setEndTime(LocalDateTime.now().plusHours(4));
        reservationRepository.create(reservation2);

        // Fetch reservations by phone number
        List<Reservation> reservations = reservationRepository.findByPhoneNumber("555123456", false);

        // Both reservations should be returned
        assertThat(reservations).hasSize(2)
                .extracting(Reservation::getId)
                .containsExactly(reservation1.getId(), reservation2.getId());
    }

    @Test
    void testFindByPhoneNumberFutureReservations() {
        // Create a Surface
        Surface surface = new Surface();
        surface.setName("Clay");
        surface.setPricePerMinute(BigDecimal.valueOf(5));
        surfaceRepository.create(surface);

        // Create a Court with a Surface
        Court court = new Court();
        court.setName("Court F");
        court.setSurface(surface);
        courtRepository.create(court);

        // Create a User
        User user = new User();
        user.setPhoneNumber("555123456");
        user.setDeleted(false);
        userRepository.create(user);

        // Create Future and Past Reservations for the User
        Reservation reservation1 = new Reservation();
        reservation1.setCourt(court);
        reservation1.setUser(user);
        reservation1.setStartTime(LocalDateTime.now().plusHours(1));
        reservation1.setEndTime(LocalDateTime.now().plusHours(2));
        reservationRepository.create(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setCourt(court);
        reservation2.setUser(user);
        reservation2.setStartTime(LocalDateTime.now().minusHours(1));  // Past reservation
        reservation2.setEndTime(LocalDateTime.now());  // Past reservation
        reservationRepository.create(reservation2);

        // Fetch future reservations by phone number
        List<Reservation> futureReservations = reservationRepository.findByPhoneNumber("555123456", true);

        // Only future reservation should be returned
        assertThat(futureReservations).hasSize(1)
                .extracting(Reservation::getId)
                .containsExactly(reservation1.getId());
    }

    @Test
    void testDelete() {
        // Create a Surface
        Surface surface = new Surface();
        surface.setName("Hard");
        surface.setPricePerMinute(BigDecimal.valueOf(6));
        surfaceRepository.create(surface);

        // Create a Court with a Surface
        Court court = new Court();
        court.setName("Court G");
        court.setSurface(surface);
        courtRepository.create(court);

        // Create a User
        User user = new User();
        user.setPhoneNumber("123654789");
        user.setDeleted(false);
        userRepository.create(user);

        // Create a Reservation for the Court and User
        Reservation reservation = new Reservation();
        reservation.setCourt(court);
        reservation.setUser(user);
        reservation.setStartTime(LocalDateTime.now().plusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(2));
        reservationRepository.create(reservation);

        // Delete the Reservation
        reservationRepository.delete(reservation.getId());

        // Validate that the reservation is marked as deleted (soft delete)
        Reservation deletedReservation = reservationRepository.findById(reservation.getId());
        assertThat(deletedReservation).isNotNull();
        assertThat(deletedReservation.isDeleted()).isTrue();
    }
}
