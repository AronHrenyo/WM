package com.wm.controller.rest;

import com.wm.entity.SalesOrderLine;
import com.wm.service.SalesOrderLineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-order-lines")
@RequiredArgsConstructor
public class SalesOrderLineRestController {

    private final SalesOrderLineService service;

    @GetMapping
    public List<SalesOrderLine> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SalesOrderLine getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public SalesOrderLine create(@RequestBody @Valid SalesOrderLine line) {
        if (line.getSalesOrder() == null || line.getSalesOrder().getSalesOrderId() == null) {
            throw new RuntimeException("SalesOrder must be specified for the line");
        }
        return service.create(line);
    }

    @PutMapping("/{id}")
    public SalesOrderLine update(@PathVariable Long id,
                                 @RequestBody @Valid SalesOrderLine line) {
        if (line.getSalesOrder() == null || line.getSalesOrder().getSalesOrderId() == null) {
            throw new RuntimeException("SalesOrder must be specified for the line");
        }
        return service.update(id, line);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}