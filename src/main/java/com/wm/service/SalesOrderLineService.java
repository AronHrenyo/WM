package com.wm.service;

import com.wm.entity.SalesOrder;
import com.wm.entity.SalesOrderLine;
import com.wm.repository.SalesOrderLineRepository;
import com.wm.repository.SalesOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesOrderLineService {

    private final SalesOrderLineRepository repository;
    private final SalesOrderRepository orderRepository;

    // List all order lines
    public List<SalesOrderLine> findAll() {
        return repository.findAll();
    }

    // Find order line by ID
    public SalesOrderLine findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order line not found with id: " + id));
    }

    // Create a new order line
    public SalesOrderLine create(SalesOrderLine line) {
        // Calculate line totals
        calculateLineSums(line);

        // Save line
        SalesOrderLine saved = repository.save(line);

        // Update parent order totals
        updateOrderTotals(line.getSalesOrder());

        return saved;
    }

    // Update an existing order line
    public SalesOrderLine update(Long id, SalesOrderLine line) {
        SalesOrderLine existing = findById(id);

        existing.setProduct(line.getProduct());
        existing.setSalesOrderLinePrice(line.getSalesOrderLinePrice());
        existing.setSalesOrderLineVatKey(line.getSalesOrderLineVatKey());
        existing.setSalesOrderLineQuantity(line.getSalesOrderLineQuantity());

        // Recalculate totals for the line
        calculateLineSums(existing);

        SalesOrderLine saved = repository.save(existing);

        // Update parent order totals
        updateOrderTotals(existing.getSalesOrder());

        return saved;
    }

    // Delete an order line
    public void delete(Long id) {
        SalesOrderLine line = findById(id);
        SalesOrder order = line.getSalesOrder();

        repository.delete(line);

        // Update parent order totals after deletion
        updateOrderTotals(order);
    }

    // 🔹 Helper methods

    public void calculateLineSums(SalesOrderLine line) {
        BigDecimal qty = BigDecimal.valueOf(line.getSalesOrderLineQuantity());
        BigDecimal price = line.getSalesOrderLinePrice();

        BigDecimal net = price.multiply(qty);

        BigDecimal vat = net.multiply(BigDecimal.valueOf(line.getSalesOrderLineVatKey()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal gross = net.add(vat);

        line.setSalesOrderLineNetSum(net);
        line.setSalesOrderLineVatSum(vat);
        line.setSalesOrderLineGrossSum(gross);
    }

    private void updateOrderTotals(SalesOrder order) {
        List<SalesOrderLine> lines =
                repository.findBySalesOrder_SalesOrderId(order.getSalesOrderId());

        BigDecimal netTotal = lines.stream()
                .map(SalesOrderLine::getSalesOrderLineNetSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal vatTotal = lines.stream()
                .map(SalesOrderLine::getSalesOrderLineVatSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal grossTotal = lines.stream()
                .map(SalesOrderLine::getSalesOrderLineGrossSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setSalesOrderNetSum(netTotal);
        order.setSalesOrderVatSum(vatTotal);
        order.setSalesOrderGrossSum(grossTotal);

        orderRepository.save(order);
    }
}