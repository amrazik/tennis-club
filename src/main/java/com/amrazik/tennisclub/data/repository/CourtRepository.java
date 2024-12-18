package com.amrazik.tennisclub.data.repository;

import com.amrazik.tennisclub.data.model.Court;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing {@link Court} entities.
 *
 * This class extends {@link MyAbstractRepository} to provide specific implementations for managing
 * {@link Court} entities. It includes methods to query for courts, ensuring that both the court
 * and its associated surface are not marked as deleted.
 *
 * The repository is annotated with {@link Repository}, indicating it's a Spring Data component that
 * will be managed by Spring's dependency injection framework.
 */
@Repository
public class CourtRepository extends MyAbstractRepository<Court> {

    /**
     * Constructs a new {@link CourtRepository} for managing {@link Court} entities.
     */
    public CourtRepository() {
        super(Court.class);
    }

    /**
     * {@inheritDoc}
     *
     * This method overrides the {@link MyAbstractRepository#findAll()} method to return a list of {@link Court}
     * entities that are not marked as deleted, and whose associated surface is also not deleted.
     */
    @Override
    public List<Court> findAll() {
        return entityManager.createQuery("SELECT c FROM Court c " +
                        "WHERE c.deleted = false " +
                        "AND c.surface.deleted = false", Court.class)
                .getResultList();
    }
}
