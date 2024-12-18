package com.amrazik.tennisclub.data.model;


/**
 * Base interface for entities with common fields.
 *
 * This interface defines the basic structure for entities that include an ID
 * and a flag to indicate whether the entity has been deleted. It is meant to
 * be implemented by other entities to ensure consistency in handling their
 * unique identifier and deletion status.
 */
public interface BaseEntity {
    Long getId();
    void setId(Long id);
    boolean isDeleted();
    void setDeleted(boolean deleted);
}
