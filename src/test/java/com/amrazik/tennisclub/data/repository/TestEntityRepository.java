package com.amrazik.tennisclub.data.repository;

import com.amrazik.tennisclub.data.model.TestEntity;
import org.springframework.stereotype.Repository;

@Repository
public class TestEntityRepository extends MyAbstractRepository<TestEntity> {

    public TestEntityRepository() {
        super(TestEntity.class);
    }
}
