package com.amrazik.tennisclub.data.repository;

import com.amrazik.tennisclub.data.model.Surface;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link Surface} entities.
 *
 * This class extends {@link MyAbstractRepository} to provide specific implementations for managing
 * {@link Surface} entities. It leverages the base repository functionality to perform CRUD operations
 * while ensuring that the entity is managed in the context of persistence.
 *
 * The repository is annotated with {@link Repository}, indicating it's a Spring Data component that
 * will be managed by Spring's dependency injection framework.
 */
@Repository
public class SurfaceRepository extends MyAbstractRepository<Surface> {

    /**
     * Constructs a new {@link SurfaceRepository} for managing {@link Surface} entities.
     */
    public SurfaceRepository() {
        super(Surface.class);
    }
}
