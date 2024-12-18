package com.amrazik.tennisclub.data.repository;

import com.amrazik.tennisclub.data.model.Reservation;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationRepository extends MyAbstractRepository<Reservation> {
    public ReservationRepository() {
        super(Reservation.class);
    }

    @Override
    public List<Reservation> findAll() {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r WHERE r.court.deleted = false AND r.user.deleted = false " +
                                "ORDER BY r.startTime ASC", Reservation.class)
                .getResultList();
    }

    public List<Reservation> findByCourtId(Long courtId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r JOIN FETCH r.court " +
                                "WHERE r.court.id = :courtId ORDER BY r.startTime ASC",
                        Reservation.class)
                .setParameter("courtId", courtId)
                .getResultList();
    }

    public List<Reservation> findByPhoneNumber(String phoneNumber, boolean future) {
        if (!future) {
            return entityManager.createQuery(
                            "SELECT r FROM Reservation r JOIN FETCH r.user " +
                                    "WHERE r.user.deleted = false " +
                                    "AND r.court.deleted = false " +
                                    "AND r.user.phoneNumber = :phoneNumber " +
                                    "ORDER BY r.startTime ASC",
                            Reservation.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getResultList();
        }

        return entityManager.createQuery(
                        "SELECT r FROM Reservation r JOIN FETCH r.user " +
                                "WHERE r.user.deleted = false " +
                                "AND r.court.deleted = false " +
                                "AND r.user.phoneNumber = :phoneNumber " +
                                "AND r.startTime > :currentTime ORDER BY r.startTime ASC",
                        Reservation.class)
                .setParameter("phoneNumber", phoneNumber)
                .setParameter("currentTime", LocalDateTime.now())
                .getResultList();
    }
}
