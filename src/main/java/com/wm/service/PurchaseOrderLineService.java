package com.wm.service;

import com.wm.entity.PurchaseOrder;
import com.wm.entity.PurchaseOrderLine;
import com.wm.repository.PurchaseOrderLineRepository;
import com.wm.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderLineService {

    private final PurchaseOrderLineRepository repository;
    private final PurchaseOrderRepository orderRepository;

    // List all order lines
    public List<PurchaseOrderLine> findAll() {
        return repository.findAll();
    }

    // Find order line by ID
    public PurchaseOrderLine findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order line not found with id: " + id));
    }

    // Create a new order line
    public PurchaseOrderLine create(PurchaseOrderLine line) {
        // Calculate line totals
        calculateLineSums(line);

        // Save line
        PurchaseOrderLine saved = repository.save(line);

        // Update parent order totals
        updateOrderTotals(line.getPurchaseOrder());

        return saved;
    }

    // Update an existing order line
    public PurchaseOrderLine update(Long id, PurchaseOrderLine line) {
        PurchaseOrderLine existing = findById(id);

        existing.setProduct(line.getProduct());
        existing.setPurchaseOrderLinePrice(line.getPurchaseOrderLinePrice());
        existing.setPurchaseOrderLineVatKey(line.getPurchaseOrderLineVatKey());
        existing.setPurchaseOrderLineQuantity(line.getPurchaseOrderLineQuantity());

        // Recalculate totals for the line
        calculateLineSums(existing);

        PurchaseOrderLine saved = repository.save(existing);

        // Update parent order totals
        updateOrderTotals(existing.getPurchaseOrder());

        return saved;
    }

    // Delete an order line
    public void delete(Long id) {
        PurchaseOrderLine line = findById(id);
        PurchaseOrder order = line.getPurchaseOrder();

        repository.delete(line);

        // Update parent order totals after deletion
        updateOrderTotals(order);
    }

    // 🔹 Helper methods

    public void calculateLineSums(PurchaseOrderLine line) {
        BigDecimal qty = BigDecimal.valueOf(line.getPurchaseOrderLineQuantity());
        BigDecimal price = line.getPurchaseOrderLinePrice();
        BigDecimal net = price.multiply(qty);
        BigDecimal vat = net.multiply(BigDecimal.valueOf(line.getPurchaseOrderLineVatKey()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal gross = net.add(vat);

        line.setPurchaseOrderLineNetSum(net);
        line.setPurchaseOrderLineVatSum(vat);
        line.setPurchaseOrderLineGrossSum(gross);
    }

    private void updateOrderTotals(PurchaseOrder order) {
        List<PurchaseOrderLine> lines = repository.findByPurchaseOrder_PurchaseOrderId(order.getPurchaseOrderId());

        BigDecimal netTotal = lines.stream()
                .map(PurchaseOrderLine::getPurchaseOrderLineNetSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal vatTotal = lines.stream()
                .map(PurchaseOrderLine::getPurchaseOrderLineVatSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal grossTotal = lines.stream()
                .map(PurchaseOrderLine::getPurchaseOrderLineGrossSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setPurchaseOrderNetSum(netTotal);
        order.setPurchaseOrderVatSum(vatTotal);
        order.setPurchaseOrderGrossSum(grossTotal);

        orderRepository.save(order);
    }
}