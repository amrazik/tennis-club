package com.amrazik.tennisclub.data.DTO;

import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for reservation details.
 *
 * This class is used to provide reservation information to clients.
 * It includes details such as the reservation ID, associated court and user,
 * start and end times, whether the reservation is for a doubles match,
 * and the total price for the reservation.
 */
public class ReservationResponseDTO {

    private Long id;
    private Court court;
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isDoubles;
    private BigDecimal totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isDoubles() {
        return isDoubles;
    }

    public void setDoubles(boolean doubles) {
        isDoubles = doubles;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
