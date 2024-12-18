package com.amrazik.tennisclub.data.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

/**
 * Represents a surface type for a tennis court in the system.
 *
 * The `Surface` class is mapped to a database table representing different types of surfaces available for tennis courts.
 * Each surface has a unique name, a price per minute for the use of a court with that surface, and a flag to indicate
 * if the surface has been deleted (soft deletion). The class implements the `BaseEntity` interface to ensure it has
 * an ID and deletion status.
 *
 * The `@SQLRestriction` annotation ensures that only non-deleted surfaces are retrieved from the database.
 */
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
