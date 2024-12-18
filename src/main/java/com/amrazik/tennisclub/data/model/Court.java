package com.amrazik.tennisclub.data.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

/**
 * Represents a tennis court entity in the system.
 *
 * The `Court` class is mapped to a database table representing tennis courts.
 * Each court has a unique identifier, a name, and is associated with a surface type.
 * The court also includes a flag to mark whether the court is deleted or not (soft deletion).
 *
 * The class implements the `BaseEntity` interface, ensuring that it has an ID and deletion status.
 * The `@SQLRestriction` annotation ensures that only non-deleted courts are retrieved from the database.
 */
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
