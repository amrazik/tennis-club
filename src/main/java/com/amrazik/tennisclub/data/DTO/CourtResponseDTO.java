package com.amrazik.tennisclub.data.DTO;

/**
 * Data Transfer Object (DTO) for representing a Court entity in responses.
 * Used to transfer court-related data from the server to the client layer.
 */
public class CourtResponseDTO {

    private Long id;
    private String name;
    private SurfaceDTO surface;

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

    public SurfaceDTO getSurface() {
        return surface;
    }

    public void setSurface(SurfaceDTO surface) {
        this.surface = surface;
    }
}
