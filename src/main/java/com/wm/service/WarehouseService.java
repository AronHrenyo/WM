package com.wm.service;

import com.wm.entity.Warehouse;
import com.wm.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseService {

    private final WarehouseRepository repository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    public List<Warehouse> findAll() {
        return repository.findAll();
    }

    public Warehouse findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
    }

    public Warehouse create(Warehouse warehouse) {
        // ha van location, biztosítsuk az SRID-t
        if (warehouse.getWarehouseLocation() != null) {
            warehouse.getWarehouseLocation().setSRID(4326);
        }
        return repository.save(warehouse);
    }

    public Warehouse update(Long id, Warehouse warehouse) {
        Warehouse existing = findById(id);

        existing.setWarehouseName(warehouse.getWarehouseName());
        existing.setWarehouseCapacity(warehouse.getWarehouseCapacity());

        // ÚJ: location frissítése
        if (warehouse.getWarehouseLocation() != null) {
            warehouse.getWarehouseLocation().setSRID(4326);
            existing.setWarehouseLocation(warehouse.getWarehouseLocation());
        }

        return repository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    // HELPER: Point létrehozás (Controllerből hívható)
    public Point createPoint(double lon, double lat) {
        Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
        point.setSRID(4326);
        return point;
    }

    public Warehouse findNearest(double lon, double lat) {
        return repository.findNearest(lon, lat);
    }
}