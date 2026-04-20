package com.wm.controller.rest;

import com.wm.entity.PurchaseOrderLine;
import com.wm.service.PurchaseOrderLineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-lines")
@RequiredArgsConstructor
public class PurchaseOrderLineRestController {

    private final PurchaseOrderLineService service;

    // Get all purchase order lines
    @GetMapping
    public List<PurchaseOrderLine> getAll() {
        return service.findAll();
    }

    // Get a single purchase order line by ID
    @GetMapping("/{id}")
    public PurchaseOrderLine getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create a new purchase order line
    @PostMapping
    public PurchaseOrderLine create(@RequestBody @Valid PurchaseOrderLine line) {
        // Ensure the line is associated with a purchase order
        if (line.getPurchaseOrder() == null || line.getPurchaseOrder().getPurchaseOrderId() == null) {
            throw new RuntimeException("PurchaseOrder must be specified for the line");
        }
        return service.create(line);
    }

    // Update an existing purchase order line
    @PutMapping("/{id}")
    public PurchaseOrderLine update(@PathVariable Long id,
                                    @RequestBody @Valid PurchaseOrderLine line) {
        // Ensure the line is associated with a purchase order
        if (line.getPurchaseOrder() == null || line.getPurchaseOrder().getPurchaseOrderId() == null) {
            throw new RuntimeException("PurchaseOrder must be specified for the line");
        }
        return service.update(id, line);
    }

    // Delete a purchase order line
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}