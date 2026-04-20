package com.wm.controller.rest;

import com.wm.entity.Warehouse;
import com.wm.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseRestController {

    private final WarehouseService service;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return service.findAll().stream()
                .map(this::toMap)
                .toList();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        return toMap(service.findById(id));
    }

    @PostMapping
    public Map<String, Object> create(@RequestBody @Valid Warehouse warehouse) {
        return toMap(service.create(warehouse));
    }

    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Long id,
                                      @RequestBody @Valid Warehouse warehouse) {
        return toMap(service.update(id, warehouse));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    private Map<String, Object> toMap(Warehouse w) {
        Map<String, Object> map = new HashMap<>();
        map.put("warehouseId", w.getWarehouseId());
        map.put("warehouseName", w.getWarehouseName());
        map.put("warehouseCapacity", w.getWarehouseCapacity());

        if (w.getWarehouseLocation() != null) {
            map.put("lat", w.getWarehouseLocation().getY());
            map.put("lon", w.getWarehouseLocation().getX());
        } else {
            map.put("lat", null);
            map.put("lon", null);
        }

        return map;
    }
}