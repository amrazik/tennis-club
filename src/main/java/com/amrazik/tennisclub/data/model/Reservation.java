package com.amrazik.tennisclub.data.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Represents a reservation for a tennis court in the system.
 *
 * The `Reservation` class is mapped to a database table representing court reservations.
 * Each reservation includes an ID, a reference to the court being reserved, a reference to the user making the reservation,
 * the start and end times of the reservation, whether the reservation is for doubles, the total price of the reservation,
 * and a flag to indicate if the reservation is deleted (soft deletion).
 *
 * The class implements the `BaseEntity` interface to ensure that it has an ID and deletion status.
 * The `@SQLRestriction` annotation ensures that only non-deleted reservations are retrieved from the database.
 */
@Entity
@SQLRestriction("deleted = false")
public class Reservation implements BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Court court;

    @ManyToOne(optional = false)
    private User user;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean isDoubles;

    private BigDecimal totalPrice;

    private boolean deleted = false;

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", court=" + court +
                ", user=" + user +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isDoubles=" + isDoubles +
                ", totalPrice=" + totalPrice +
                ", deleted=" + deleted +
                '}';
    }
}
