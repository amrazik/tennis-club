package com.amrazik.tennisclub.repository;

import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.Surface;
import com.amrazik.tennisclub.data.repository.CourtRepository;
import com.amrazik.tennisclub.data.repository.SurfaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CourtRepositoryTest {
    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private SurfaceRepository surfaceRepository;


    @Test
    void testCreate() {
        Surface surface = new Surface();
        surface.setName("Grass Court");
        surface.setPricePerMinute(BigDecimal.valueOf(10.0));
        surfaceRepository.create(surface);

        Court court = new Court();
        court.setName("Court A");
        court.setSurface(surface);

        Court savedCourt = courtRepository.create(court);

        assertThat(savedCourt.getId()).isNotNull();
        assertThat(savedCourt.getName()).isEqualTo("Court A");
        assertThat(savedCourt.getSurface().getName()).isEqualTo("Grass Court");
    }

    @Test
    void testFindById() {
        Surface surface = new Surface();
        surface.setName("Clay Court");
        surface.setPricePerMinute(BigDecimal.valueOf(8.0));
        surfaceRepository.create(surface);

        Court court = new Court();
        court.setName("Court B");
        court.setSurface(surface);
        courtRepository.create(court);

        Court foundCourt = courtRepository.findById(court.getId());
        assertThat(foundCourt).isNotNull();
        assertThat(foundCourt.getName()).isEqualTo("Court B");
        assertThat(foundCourt.getSurface().getName()).isEqualTo("Clay Court");
    }

    @Test
    void testFindAllWithSurfaceRestriction() {
        Surface surface1 = new Surface();
        surface1.setName("Hardcourt");
        surface1.setPricePerMinute(BigDecimal.valueOf(12.0));
        surfaceRepository.create(surface1);

        Surface surface2 = new Surface();
        surface2.setName("Synthetic Court");
        surface2.setPricePerMinute(BigDecimal.valueOf(15.0));
        surface2.setDeleted(true); // This surface should be excluded
        surfaceRepository.create(surface2);

        Court court1 = new Court();
        court1.setName("Court C");
        court1.setSurface(surface1);
        courtRepository.create(court1);

        Court court2 = new Court();
        court2.setName("Court D");
        court2.setSurface(surface2);
        courtRepository.create(court2);

        List<Court> courts = courtRepository.findAll();

        // Only court1 should be found because surface2 is marked as deleted
        assertThat(courts).hasSize(1)
                .extracting(Court::getName)
                .containsExactly("Court C");
    }

    @Test
    void testUpdate() {
        Surface surface = new Surface();
        surface.setName("Indoor Court");
        surface.setPricePerMinute(BigDecimal.valueOf(20.0));
        surfaceRepository.create(surface);

        Court court = new Court();
        court.setName("Court E");
        court.setSurface(surface);
        courtRepository.create(court);

        court.setName("Updated Court E");
        Court updatedCourt = courtRepository.update(court);

        assertThat(updatedCourt.getName()).isEqualTo("Updated Court E");
    }

    @Test
    void testDelete() {
        Surface surface = new Surface();
        surface.setName("Outdoor Court");
        surface.setPricePerMinute(BigDecimal.valueOf(5.0));
        surfaceRepository.create(surface);

        Court court = new Court();
        court.setName("Court F");
        court.setSurface(surface);
        courtRepository.create(court);

        courtRepository.delete(court.getId());

        Court deletedCourt = courtRepository.findById(court.getId());
        assertThat(deletedCourt).isNotNull();
        assertThat(deletedCourt.isDeleted()).isTrue();
    }

    @Test
    void testSoftDeleteSurfaceExclusion() {
        Surface surface1 = new Surface();
        surface1.setName("Test Surface 1");
        surface1.setPricePerMinute(BigDecimal.valueOf(10.0));
        surfaceRepository.create(surface1);

        Surface surface2 = new Surface();
        surface2.setName("Test Surface 2");
        surface2.setPricePerMinute(BigDecimal.valueOf(12.0));
        surface2.setDeleted(true); // This surface should be excluded
        surfaceRepository.create(surface2);

        Court court1 = new Court();
        court1.setName("Test Court 1");
        court1.setSurface(surface1);
        courtRepository.create(court1);

        Court court2 = new Court();
        court2.setName("Test Court 2");
        court2.setSurface(surface2);
        courtRepository.create(court2);

        // Testing findAll() with surface restriction
        List<Court> courts = courtRepository.findAll();

        // Only court1 should be included since court2 has a deleted surface
        assertThat(courts).hasSize(1)
                .extracting(Court::getName)
                .containsExactly("Test Court 1");
    }
}
