package com.amrazik.tennisclub.data.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@SQLRestriction("deleted = false")
public class Surface implements BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private BigDecimal pricePerMinute;
    private boolean deleted = false;

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
