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

@Service
@Transactional
public class DataInitializer implements CommandLineRunner {
    private final SurfaceRepository surfaceRepository;
    private final CourtRepository courtRepository;

    @Value("${app.data.init:false}")
    private boolean initData;

    @Autowired
    public DataInitializer(SurfaceRepository surfaceRepository, CourtRepository courtRepository, UserRepository userRepository) {
        this.surfaceRepository = surfaceRepository;
        this.courtRepository = courtRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (!initData) return;

        Surface clay = new Surface();
        clay.setName("Clay");
        clay.setPricePerMinute(BigDecimal.valueOf(0.5));
        surfaceRepository.create(clay);

        Surface grass = new Surface();
        grass.setName("Grass");
        grass.setPricePerMinute(BigDecimal.valueOf(0.8));
        surfaceRepository.create(grass);

        Court court;
        for (int i = 1; i <= 4; i++) {
            court = new Court();
            court.setName("Court " + i);
            court.setSurface(i % 2 == 0 ? clay : grass);
            courtRepository.create(court);
        }
    }
}
