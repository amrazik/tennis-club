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

@Mapper
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);


    ReservationResponseDTO mapToDTO(Reservation reservation);

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

    List<ReservationResponseDTO> mapToDTOList(List<Reservation> reservations);
}
