package com.amrazik.tennisclub.config;

import com.amrazik.tennisclub.data.model.Court;
import com.amrazik.tennisclub.data.model.Surface;
import com.amrazik.tennisclub.data.repository.CourtRepository;
import com.amrazik.tennisclub.data.repository.SurfaceRepository;
import com.amrazik.tennisclub.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service class responsible for initializing database data at application startup.
 * Implements the {@link CommandLineRunner} interface to execute logic after the Spring context is loaded.
 */
@Service
@Transactional
public class DataInitializer implements CommandLineRunner {

    // Repository for interacting with Surface entities.
    private final SurfaceRepository surfaceRepository;

    // Repository for interacting with Court entities.
    private final CourtRepository courtRepository;

    /**
     * Flag indicating whether data initialization should run.
     * Value is retrieved from the application configuration property `app.data.init`.
     * Defaults to `false` if not set.
     */
    @Value("${app.data.init:false}")
    private boolean initData;

    /**
     * Constructor for DataInitializer.
     *
     * @param surfaceRepository Repository for managing Surface entities.
     * @param courtRepository   Repository for managing Court entities.
     * @param userRepository    Repository for managing User entities (currently unused).
     */
    @Autowired
    public DataInitializer(SurfaceRepository surfaceRepository, CourtRepository courtRepository, UserRepository userRepository) {
        this.surfaceRepository = surfaceRepository;
        this.courtRepository = courtRepository;
    }

    /**
     * Initializes database data if the `initData` flag is set to true.
     * Creates two types of surfaces (Clay and Grass) and four courts (Court 1 to Court 4),
     * alternating between the two surfaces.
     *
     * @param args Command-line arguments (unused in this implementation).
     */
    @Override
    public void run(String... args) {
        if (!initData) return;

        // Create and save Clay surface
        Surface clay = new Surface();
        clay.setName("Clay");
        clay.setPricePerMinute(BigDecimal.valueOf(0.5));
        surfaceRepository.create(clay);

        // Create and save Grass surface
        Surface grass = new Surface();
        grass.setName("Grass");
        grass.setPricePerMinute(BigDecimal.valueOf(0.8));
        surfaceRepository.create(grass);

        // Create and save 4 courts alternating between Clay and Grass surfaces
        Court court;
        for (int i = 1; i <= 4; i++) {
            court = new Court();
            court.setName("Court " + i);
            court.setSurface(i % 2 == 0 ? clay : grass);
            courtRepository.create(court);
        }
    }
}
