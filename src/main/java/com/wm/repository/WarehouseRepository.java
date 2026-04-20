package com.wm.repository;

import com.wm.entity.SalesOrder;
import com.wm.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query(value = """
    SELECT * FROM warehouse
    ORDER BY warehouse_location <-> ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)
    LIMIT 1
    """, nativeQuery = true)
    Warehouse findNearest(double lon, double lat);
    Optional<Warehouse> findByWarehouseName(String name);
}