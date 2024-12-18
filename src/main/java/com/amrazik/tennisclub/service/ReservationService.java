package com.amrazik.tennisclub.service;

import com.amrazik.tennisclub.data.DTO.ReservationCreateDTO;
import com.amrazik.tennisclub.data.DTO.ReservationResponseDTO;
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

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper = ReservationMapper.INSTANCE;

    public ReservationService(ReservationRepository reservationRepository, CourtRepository courtRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.courtRepository = courtRepository;
        this.userRepository = userRepository;
    }

    public BigDecimal createReservation(ReservationCreateDTO reservationCreateDTO) {
        Reservation reservation = reservationMapper.mapFromDTO(reservationCreateDTO, courtRepository);
        setupUser(reservation);
        validateReservation(reservation);
        reservation.setTotalPrice(calculateTotalPrice(reservation));
        reservation = reservationRepository.create(reservation);
        return reservation.getTotalPrice();
    }

    @Transactional(readOnly = true)
    public ReservationResponseDTO getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        if (reservation == null) {
            throw new EntityNotFoundException("Reservation with id " + id + " not found");
        }
        return reservationMapper.mapToDTO(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservations() {
        return reservationMapper.mapToDTOList(reservationRepository.findAll());
    }

    public ReservationResponseDTO updateReservation(ReservationCreateDTO reservationCreateDTO) {
        Reservation reservation = reservationMapper.mapFromDTO(reservationCreateDTO, courtRepository);
        setupUser(reservation);
//        validateReservation(reservation);
        reservation.setTotalPrice(calculateTotalPrice(reservation));
        return reservationMapper.mapToDTO(reservationRepository.update(reservation));
    }

    private void setupUser(Reservation reservation) {
        User user = userRepository.findByNumber(reservation.getUser().getPhoneNumber());
        if (user == null) {
            user = userRepository.create(reservation.getUser());
        }
        reservation.setUser(user);
    }

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

    private BigDecimal calculateTotalPrice(Reservation reservation) {
        BigDecimal pricePerMinute = reservation.getCourt().getSurface().getPricePerMinute();
        long minutes = reservation.getStartTime().until(reservation.getEndTime(), ChronoUnit.MINUTES);
        BigDecimal totalPrice = pricePerMinute.multiply(BigDecimal.valueOf(minutes));
        if (!reservation.isDoubles()) {
            totalPrice = totalPrice.multiply(BigDecimal.valueOf(1.5));
        }
        System.out.println(totalPrice);
        return totalPrice;
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByCourt(Long courtId) {
        List<Reservation> reservations = reservationRepository.findByCourtId(courtId);
        return reservationMapper.mapToDTOList(reservations);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByPhoneNumber(String phoneNumber, boolean future) {
        List<Reservation> reservations = reservationRepository.findByPhoneNumber(phoneNumber, future);
        return reservationMapper.mapToDTOList(reservations);
    }
}
