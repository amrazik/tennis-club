package com.amrazik.tennisclub.data.repository;

import com.amrazik.tennisclub.data.model.Court;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourtRepository extends MyAbstractRepository<Court> {
    public CourtRepository() {
        super(Court.class);
    }

    @Override
    public List<Court> findAll() {
        return entityManager.createQuery("SELECT c FROM Court c " +
                        "WHERE c.deleted = false " +
                        "AND c.surface.deleted = false", Court.class)
                .getResultList();
    }
}
