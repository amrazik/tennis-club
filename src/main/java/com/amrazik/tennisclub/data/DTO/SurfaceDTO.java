package com.amrazik.tennisclub.data.DTO;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for surface details.
 *
 * This class is used to transfer data related to court surfaces
 * between different layers of the application. It includes the
 * surface ID, name, and the price per minute for using the surface.
 */
public class SurfaceDTO {
    private Long id;
    private String name;
    private BigDecimal pricePerMinute;

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

    public BigDecimal getPricePerMinute() {
        return pricePerMinute;
    }

    public void setPricePerMinute(BigDecimal pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }
}
