package com.amrazik.tennisclub.data.DTO;

import java.math.BigDecimal;

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
