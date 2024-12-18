package com.amrazik.tennisclub.data.repository;

import com.amrazik.tennisclub.data.model.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

/**
 * A generic abstract repository implementation for basic CRUD operations.
 *
 * This class provides a default implementation of the CRUD operations defined in the {@link MyRepository} interface
 * for entities that extend {@link BaseEntity}. It uses Jakarta Persistence API (JPA) and the {@link EntityManager}
 * to manage database operations. The `deleted` flag is used for soft deletes, meaning entities are not permanently
 * removed from the database but marked as deleted.
 *
 * @param <T> the type of entity the repository manages, which must extend {@link BaseEntity}.
 */
public class MyAbstractRepository<T extends BaseEntity> implements MyRepository<T> {

    @PersistenceContext
    EntityManager entityManager;

    private final Class<T> entityType;

    /**
     * Constructs a new instance of the repository for the specified entity type.
     *
     * @param entityType the class type of the entity the repository manages.
     */
    public MyAbstractRepository(Class<T> entityType) {
        this.entityType = entityType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T findById(Long id) {
        System.out.println("Finding entity with id: " + id);
        return entityManager.find(entityType, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> findAll() {
        String query = "SELECT e FROM " + entityType.getSimpleName() + " e WHERE e.deleted = false";
        return entityManager.createQuery(query, entityType)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        T entity = findById(id);
        entity.setDeleted(true);
        entityManager.merge(entity);
    }

    /**
     * Deletes all entities by setting their {@code deleted} flag to {@code true}.
     */
    public void deleteAll() {
        String query = "UPDATE " + entityType.getSimpleName() + " e SET e.deleted = true";
        entityManager.createQuery(query).executeUpdate();
    }
}
