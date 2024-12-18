package com.amrazik.tennisclub.integration;

import com.amrazik.tennisclub.controller.CourtController;
import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.Surface;
import com.amrazik.tennisclub.data.repository.CourtRepository;
import com.amrazik.tennisclub.data.repository.SurfaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class CourtIntegrationTest {

    @Autowired
    private SurfaceRepository surfaceRepository;

    @Autowired
    private CourtRepository courtRepository;

    private MockMvc mockMvc;

    @Autowired
    private CourtController courtController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courtController).build();
    }

    @Test
    void testCreateCourt_Success() throws Exception {
        // Arrange
        Surface surface = new Surface();
        surface.setName("Clay");
        surface.setPricePerMinute(BigDecimal.valueOf(1.0));
        Surface savedSurface = surfaceRepository.create(surface);

        String courtCreateJson = "{\"name\": \"Court 1\", \"surfaceId\": " + savedSurface.getId() + "}";

        // Act & Assert
        mockMvc.perform(post("/courts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courtCreateJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Court 1"))
                .andExpect(jsonPath("$.surface.name").value("Clay"));
    }

    @Test
    void testGetCourtById_Success() throws Exception {
        // Arrange
        Surface surface = new Surface();
        surface.setName("Grass");
        surface.setPricePerMinute(BigDecimal.valueOf(1.5));
        Surface savedSurface = surfaceRepository.create(surface);

        Court court = new Court();
        court.setName("Court 2");
        court.setSurface(savedSurface);
        Court savedCourt = courtRepository.create(court);

        // Act & Assert
        mockMvc.perform(get("/courts/" + savedCourt.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Court 2"))
                .andExpect(jsonPath("$.surface.name").value("Grass"));
    }

    @Test
    void testUpdateCourt_Success() throws Exception {
        // Arrange
        Surface surface = new Surface();
        surface.setName("Hard");
        surface.setPricePerMinute(BigDecimal.valueOf(2.0));
        Surface savedSurface = surfaceRepository.create(surface);

        Court court = new Court();
        court.setName("Court 3");
        court.setSurface(savedSurface);
        Court savedCourt = courtRepository.create(court);

        String updateJson = "{\"id\": " + savedCourt.getId() + ", \"name\": \"Updated Court 3\", \"surfaceId\": " + savedSurface.getId() + "}";

        // Act & Assert
        mockMvc.perform(put("/courts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Court 3"))
                .andExpect(jsonPath("$.surface.name").value("Hard"));
    }

    @Test
    void testDeleteCourt_Success() throws Exception {
        // Arrange
        Surface surface = new Surface();
        surface.setName("Synthetic");
        surface.setPricePerMinute(BigDecimal.valueOf(1.2));
        Surface savedSurface = surfaceRepository.create(surface);

        Court court = new Court();
        court.setName("Court 4");
        court.setSurface(savedSurface);
        Court savedCourt = courtRepository.create(court);

        // Act & Assert
        mockMvc.perform(delete("/courts/" + savedCourt.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Court deletedCourt = courtRepository.findById(savedCourt.getId());
        assert (deletedCourt.isDeleted());
    }
}
