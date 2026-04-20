package com.wm.service;

import com.wm.entity.SalesOrder;
import com.wm.repository.SalesOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesOrderService {

    private final SalesOrderRepository repository;

    // List all sales orders
    public List<SalesOrder> findAll() {
        return repository.findAll();
    }

    // Find a sales order by ID
    public SalesOrder findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with id: " + id));
    }

    // Create a new sales order
    public SalesOrder create(SalesOrder order) {
        // Check for duplicate order number
        repository.findBySalesOrderNumber(order.getSalesOrderNumber())
                .ifPresent(o -> {
                    throw new RuntimeException("Sales order number already exists: " + order.getSalesOrderNumber());
                });

        // Set default date and status
        order.setSalesOrderDate(LocalDate.now());
        order.setSalesOrderStatus("NEW");

        // Initialize totals
        order.setSalesOrderNetSum(BigDecimal.ZERO);
        order.setSalesOrderVatSum(BigDecimal.ZERO);
        order.setSalesOrderGrossSum(BigDecimal.ZERO);

        return repository.save(order);
    }

    // Update an existing sales order
    public SalesOrder update(Long id, SalesOrder order) {
        SalesOrder existing = findById(id);

        existing.setSalesOrderNumber(order.getSalesOrderNumber());
        existing.setSalesOrderStatus(order.getSalesOrderStatus());

        return repository.save(existing);
    }

    // Delete a sales order
    public void delete(Long id) {
        findById(id); // ensure it exists
        repository.deleteById(id);
    }
}