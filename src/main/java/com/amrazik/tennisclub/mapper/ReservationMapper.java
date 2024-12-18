package com.amrazik.tennisclub.mapper;

import com.amrazik.tennisclub.data.DTO.ReservationCreateDTO;
import com.amrazik.tennisclub.data.DTO.ReservationResponseDTO;
import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.Reservation;
import com.amrazik.tennisclub.data.model.User;
import com.amrazik.tennisclub.data.repository.CourtRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper interface for converting between {@link Reservation} entities and their corresponding DTOs.
 *
 * This interface defines methods for mapping {@link Reservation} entities to {@link ReservationResponseDTO} objects
 * and vice versa. It utilizes the MapStruct framework to automatically generate the implementation
 * for the defined mapping methods. The {@link ReservationMapper} includes a default method for converting
 * {@link ReservationCreateDTO} to {@link Reservation} with additional logic to fetch the related {@link Court}
 * entity from the {@link CourtRepository}.
 */
@Mapper
public interface ReservationMapper {

    /**
     * An instance of {@link ReservationMapper} for use in the application.
     */
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    /**
     * Maps a {@link Reservation} entity to a {@link ReservationResponseDTO}.
     *
     * @param reservation The {@link Reservation} entity to be mapped.
     * @return The mapped {@link ReservationResponseDTO} object.
     */
    ReservationResponseDTO mapToDTO(Reservation reservation);

    /**
     * Maps a {@link ReservationCreateDTO} to a {@link Reservation} entity.
     *
     * This method retrieves the associated {@link Court} entity using its ID from the
     * {@link CourtRepository}. If no {@link Court} is found with the provided ID, an exception
     * will be thrown.
     *
     * @param reservationCreateDTO The {@link ReservationCreateDTO} containing the data to be mapped.
     * @param courtRepository The repository used to retrieve the {@link Court} entity.
     * @return The mapped {@link Reservation} entity.
     * @throws IllegalArgumentException If the {@link ReservationCreateDTO} does not provide a valid courtId.
     * @throws EntityNotFoundException If no {@link Court} entity is found for the provided courtId.
     */
    @Mapping(source = "courtId", target = "court")
    default Reservation mapFromDTO(ReservationCreateDTO reservationCreateDTO, CourtRepository courtRepository) {
        Long courtId = reservationCreateDTO.getCourtId();
        if (courtId == null) {
            throw new IllegalArgumentException("Missing courtId");
        }
        Court court = courtRepository.findById(courtId);
        if (court == null) {
            throw new EntityNotFoundException("Court with id " + courtId + " not found");
        }

        return getReservation(reservationCreateDTO, court);
    }

    /**
     * Helper method to create a {@link Reservation} entity from a {@link ReservationCreateDTO}
     * and the associated {@link Court} entity.
     *
     * @param reservationCreateDTO The {@link ReservationCreateDTO} containing the data to be mapped.
     * @param court The {@link Court} associated with the reservation.
     * @return The mapped {@link Reservation} entity.
     */
    private static Reservation getReservation(ReservationCreateDTO reservationCreateDTO, Court court) {
        Reservation reservation = new Reservation();
        User user = new User();
        user.setName(reservationCreateDTO.getUserName());
        user.setPhoneNumber(reservationCreateDTO.getPhoneNumber());
        reservation.setId(reservationCreateDTO.getId());
        reservation.setCourt(court);
        reservation.setUser(user);
        reservation.setStartTime(reservationCreateDTO.getStartTime());
        reservation.setEndTime(reservationCreateDTO.getEndTime());
        reservation.setDoubles(reservationCreateDTO.isDoubles());
        return reservation;
    }

    /**
     * Maps a list of {@link Reservation} entities to a list of {@link ReservationResponseDTO} objects.
     *
     * @param reservations The list of {@link Reservation} entities to be mapped.
     * @return The list of mapped {@link ReservationResponseDTO} objects.
     */
    List<ReservationResponseDTO> mapToDTOList(List<Reservation> reservations);
}
