package com.amrazik.tennisclub.data.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLRestriction("deleted = false")
public class Court implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private Surface surface;
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

    public Surface getSurface() {
        return surface;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
