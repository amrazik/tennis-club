package com.amrazik.tennisclub.data.repository;

import java.util.List;

/**
 * A generic repository interface for basic CRUD operations.
 *
 * This interface defines common methods for interacting with a database for any entity type `T`. It provides
 * standard operations such as creating, finding, updating, and deleting entities. These methods allow for
 * database persistence operations to be abstracted and implemented for specific entities.
 *
 * @param <T> the type of entity the repository manages (e.g., `Court`, `User`, `Reservation`).
 */
public interface MyRepository<T> {

    /**
     * Creates a new entity in the database.
     *
     * @param t the entity to be created.
     * @return the created entity.
     */
    T create(T t);

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity.
     * @return the entity if found, or {@code null} if not found.
     */
    T findById(Long id);

    /**
     * Retrieves all entities from the database.
     *
     * @return a list of all entities.
     */
    List<T> findAll();

    /**
     * Updates an existing entity in the database.
     *
     * @param t the entity to be updated.
     * @return the updated entity.
     */
    T update(T t);

    /**
     * Deletes an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to be deleted.
     */
    void delete(Long id);

    /**
     * Deletes all entities from the database.
     */
    void deleteAll();
}
