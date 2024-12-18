package com.amrazik.tennisclub.data.repository;

import com.amrazik.tennisclub.data.model.Surface;
import org.springframework.stereotype.Repository;

@Repository
public class SurfaceRepository extends MyAbstractRepository<Surface> {
    public SurfaceRepository() {
        super(Surface.class);
    }
}
