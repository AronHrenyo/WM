package com.wm.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseOrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer purchaseOrderId;
    private String purchaseOrderNumber;
    private LocalDate purchaseOrderDate;
    private String purchaseOrderStatus;
    @Column(precision = 10, scale = 2)
    private BigDecimal purchaseOrderPriceSum;
    @Column(precision = 10, scale = 2)
    private BigDecimal purchaseOrderVatSum;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderLineModel> purchaseOrderLines;

    public void addLine(PurchaseOrderLineModel line) {
        purchaseOrderLines.add(line);
        line.setPurchaseOrder(this);
    }
}
