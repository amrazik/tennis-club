package com.amrazik.tennisclub.data.DTO;

/**
 * Data Transfer Object (DTO) for creating or updating a Court entity.
 * Used to transfer court-related data between the client and server layers.
 */
public class CourtCreateDTO {

    private Long id;
    private String name;
    private Long surfaceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSurfaceId() {
        return surfaceId;
    }

    public void setSurfaceId(Long surfaceId) {
        this.surfaceId = surfaceId;
    }
}
