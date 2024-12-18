package com.amrazik.tennisclub.service;

import com.amrazik.tennisclub.data.DTO.ReservationCreateDTO;
import com.amrazik.tennisclub.data.DTO.ReservationResponseDTO;
import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.Reservation;
import com.amrazik.tennisclub.data.model.User;
import com.amrazik.tennisclub.data.repository.CourtRepository;
import com.amrazik.tennisclub.data.repository.ReservationRepository;
import com.amrazik.tennisclub.data.repository.UserRepository;
import com.amrazik.tennisclub.mapper.ReservationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service class responsible for handling business logic related to {@link Reservation} entities.
 *
 * The {@link ReservationService} class manages operations for creating, retrieving, updating, deleting,
 * and calculating the total price for {@link Reservation} entities. It interacts with the {@link ReservationRepository},
 * {@link CourtRepository}, and {@link UserRepository} for data persistence and retrieval. This service ensures
 * that business logic is applied correctly and that data integrity is maintained. Additionally, it validates the
 * reservation's duration, checks for conflicts with existing reservations, and calculates the total price based
 * on the court's pricing and reservation duration.
 */
@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper = ReservationMapper.INSTANCE;

    /**
     * Constructs a new {@link ReservationService} with the provided repositories.
     *
     * @param reservationRepository The {@link ReservationRepository} used for persistence operations on {@link Reservation} entities.
     * @param courtRepository The {@link CourtRepository} used for retrieving {@link Court} entities.
     * @param userRepository The {@link UserRepository} used for retrieving {@link User} entities.
     */
    public ReservationService(ReservationRepository reservationRepository, CourtRepository courtRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.courtRepository = courtRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new {@link Reservation} based on the provided {@link ReservationCreateDTO}.
     *
     * The method maps the DTO to a {@link Reservation} entity, sets up the associated {@link User}, validates the reservation,
     * calculates its total price, and persists the reservation. The total price is returned after successful creation.
     *
     * @param reservationCreateDTO The DTO containing the data for the new {@link Reservation}.
     * @return The total price of the created {@link Reservation}.
     */
    public BigDecimal createReservation(ReservationCreateDTO reservationCreateDTO) {
        Reservation reservation = reservationMapper.mapFromDTO(reservationCreateDTO, courtRepository);
        setupUser(reservation);
        validateReservation(reservation);
        reservation.setTotalPrice(calculateTotalPrice(reservation));
        reservation = reservationRepository.create(reservation);
        return reservation.getTotalPrice();
    }

    /**
     * Retrieves a {@link Reservation} by its ID and returns it as a {@link ReservationResponseDTO}.
     *
     * This method checks if the {@link Reservation} exists in the database. If not, an {@link EntityNotFoundException}
     * is thrown. It uses the {@link ReservationMapper} to map the {@link Reservation} entity to a DTO.
     *
     * @param id The ID of the {@link Reservation} to retrieve.
     * @return The DTO representation of the {@link Reservation}.
     * @throws EntityNotFoundException If no {@link Reservation} with the specified ID is found.
     */
    @Transactional(readOnly = true)
    public ReservationResponseDTO getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        if (reservation == null) {
            throw new EntityNotFoundException("Reservation with id " + id + " not found");
        }
        return reservationMapper.mapToDTO(reservation);
    }

    /**
     * Retrieves all {@link Reservation}s and returns them as a list of {@link ReservationResponseDTO}s.
     *
     * @return A list of DTO representations of all {@link Reservation}s.
     */
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservations() {
        return reservationMapper.mapToDTOList(reservationRepository.findAll());
    }

    /**
     * Updates an existing {@link Reservation} based on the provided {@link ReservationCreateDTO}.
     *
     * The method maps the DTO to a {@link Reservation} entity, sets up the associated {@link User},
     * validates the reservation, and calculates its total price before updating it in the database.
     *
     * @param reservationCreateDTO The DTO containing the updated data for the {@link Reservation}.
     * @return The DTO representation of the updated {@link Reservation}.
     */
    public ReservationResponseDTO updateReservation(ReservationCreateDTO reservationCreateDTO) {
        Reservation reservation = reservationMapper.mapFromDTO(reservationCreateDTO, courtRepository);
        setupUser(reservation);
        reservation.setTotalPrice(calculateTotalPrice(reservation));
        return reservationMapper.mapToDTO(reservationRepository.update(reservation));
    }

    /**
     * Sets up the {@link User} for the given {@link Reservation}. If the user doesn't exist, a new user is created.
     *
     * @param reservation The {@link Reservation} to associate with a {@link User}.
     */
    private void setupUser(Reservation reservation) {
        User user = userRepository.findByNumber(reservation.getUser().getPhoneNumber());
        if (user == null) {
            user = userRepository.create(reservation.getUser());
        }
        reservation.setUser(user);
    }

    /**
     * Validates the given {@link Reservation}. This includes checking that the reservation duration is at least 10 minutes
     * and that there are no conflicts with existing reservations for the same court.
     *
     * @param reservation The {@link Reservation} to validate.
     * @throws IllegalArgumentException If the reservation duration is too short or there are conflicts with existing reservations.
     */
    private void validateReservation(Reservation reservation) {
        long durationInMinutes = reservation.getStartTime().until(reservation.getEndTime(), ChronoUnit.MINUTES);
        if (durationInMinutes < 10) {
            throw new IllegalArgumentException("Reservation has to be atleast 10 minutes long");
        }
        List<Reservation> reservations = reservationRepository.findByCourtId(reservation.getCourt().getId());
        for (Reservation existingReservation : reservations) {
            if (reservation.getStartTime().isBefore(existingReservation.getEndTime())
                    && reservation.getEndTime().isAfter(existingReservation.getStartTime())) {
                throw new IllegalArgumentException("Court is already reserved at this time");
            }
        }
    }

    /**
     * Calculates the total price for the given {@link Reservation}.
     *
     * The price is calculated based on the court's pricing per minute and the reservation duration.
     * If the reservation is for doubles, a multiplier is applied to the total price.
     *
     * @param reservation The {@link Reservation} for which to calculate the total price.
     * @return The total price of the reservation.
     */
    private BigDecimal calculateTotalPrice(Reservation reservation) {
        BigDecimal pricePerMinute = reservation.getCourt().getSurface().getPricePerMinute();
        long minutes = reservation.getStartTime().until(reservation.getEndTime(), ChronoUnit.MINUTES);
        BigDecimal totalPrice = pricePerMinute.multiply(BigDecimal.valueOf(minutes));
        if (!reservation.isDoubles()) {
            totalPrice = totalPrice.multiply(BigDecimal.valueOf(1.5));
        }
        return totalPrice;
    }

    /**
     * Deletes a {@link Reservation} by its ID.
     *
     * The method removes the {@link Reservation} from the database.
     *
     * @param id The ID of the {@link Reservation} to delete.
     */
    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }

    /**
     * Retrieves all {@link Reservation}s for a given court and returns them as a list of {@link ReservationResponseDTO}s.
     *
     * @param courtId The ID of the court to retrieve reservations for.
     * @return A list of DTO representations of the {@link Reservation}s for the specified court.
     */
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByCourt(Long courtId) {
        List<Reservation> reservations = reservationRepository.findByCourtId(courtId);
        return reservationMapper.mapToDTOList(reservations);
    }

    /**
     * Retrieves all {@link Reservation}s made by a user identified by their phone number.
     * If the `future` parameter is set to true, only future reservations will be returned.
     *
     * @param phoneNumber The phone number of the user to retrieve reservations for.
     * @param future Whether to only retrieve future reservations.
     * @return A list of DTO representations of the user's {@link Reservation}s.
     */
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByPhoneNumber(String phoneNumber, boolean future) {
        List<Reservation> reservations = reservationRepository.findByPhoneNumber(phoneNumber, future);
        return reservationMapper.mapToDTOList(reservations);
    }
}
