package com.amrazik.tennisclub.data.repository;

import com.amrazik.tennisclub.data.model.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class MyAbstractRepository<T extends BaseEntity> implements MyRepository<T> {
    @PersistenceContext
    EntityManager entityManager;

    private final Class<T> entityType;

    public MyAbstractRepository(Class<T> entityType) {
        this.entityType = entityType;
    }

    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T findById(Long id) {
        System.out.println("Finding entity with id: " + id);
        return entityManager.find(entityType, id);
    }

    @Override
    public List<T> findAll() {
        String query = "SELECT e FROM " + entityType.getSimpleName() + " e WHERE e.deleted = false";
        return entityManager.createQuery(query, entityType)
                .getResultList();
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(Long id) {
        T entity = findById(id);
        entity.setDeleted(true);
        entityManager.merge(entity);
    }

    public void deleteAll() {
        String query = "UPDATE " + entityType.getSimpleName() + " e SET e.deleted = true";
        entityManager.createQuery(query).executeUpdate();
    }
}
