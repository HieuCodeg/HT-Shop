package com.hieucodeg.service.location;

import com.hieucodeg.model.LocationRegion;
import com.hieucodeg.repository.LocationRegionRepositiry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class LocationRegionImp implements ILocationRegionService {

    @Autowired
    private LocationRegionRepositiry locationRegionRepositiry;
    @Override
    public List<LocationRegion> findAll() {
        return null;
    }

    @Override
    public LocationRegion getById(Long id) {
        return null;
    }

    @Override
    public Optional<LocationRegion> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public LocationRegion save(LocationRegion locationRegion) {
        return locationRegionRepositiry.save(locationRegion);
    }

    @Override
    public void remove(Long id) {

    }
}
