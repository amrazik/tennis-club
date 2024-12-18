package com.amrazik.tennisclub.data.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ReservationCreateDTO {
    private Long id;
    private Long courtId;
    private String userName;
    private String phoneNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @JsonProperty("isDoubles")
    private boolean isDoubles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}
