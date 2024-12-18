package com.amrazik.tennisclub.repository;

import com.amrazik.tennisclub.data.model.TestEntity;
import com.amrazik.tennisclub.data.repository.TestEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MyAbstractRepositoryTest {

    @Autowired
    private TestEntityRepository testEntityRepository;

    @Test
    void testCreate() {
        TestEntity entity = new TestEntity();
        entity.setName("Entity A");

        TestEntity savedEntity = testEntityRepository.create(entity);

        assertThat(savedEntity.getId()).isNotNull();
        assertThat(savedEntity.getName()).isEqualTo("Entity A");
    }

    @Test
    void testFindById() {
        TestEntity entity = new TestEntity();
        entity.setName("Entity B");
        testEntityRepository.create(entity);
        TestEntity foundEntity = testEntityRepository.findById(entity.getId());
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getName()).isEqualTo("Entity B");
    }

    @Test
    void testFindAll() {
        TestEntity entity1 = new TestEntity();
        entity1.setName("Entity C");
        testEntityRepository.create(entity1);

        TestEntity entity2 = new TestEntity();
        entity2.setName("Entity D");
        testEntityRepository.create(entity2);

        List<TestEntity> entities = testEntityRepository.findAll();
        assertThat(entities).hasSize(2).extracting(TestEntity::getName).contains("Entity C", "Entity D");
    }

    @Test
    void testUpdate() {
        TestEntity entity = new TestEntity();
        entity.setName("Old Name");
        testEntityRepository.create(entity);

        entity.setName("New Name");
        TestEntity updatedEntity = testEntityRepository.update(entity);

        assertThat(updatedEntity.getName()).isEqualTo("New Name");
    }

    @Test
    void testDelete() {
        TestEntity entity = new TestEntity();
        entity.setName("Entity E");
        testEntityRepository.create(entity);

        testEntityRepository.delete(entity.getId());

        TestEntity deletedEntity = testEntityRepository.findById(entity.getId());
        assertThat(deletedEntity).isNotNull();
        assertThat(deletedEntity.isDeleted()).isTrue();
    }
}
