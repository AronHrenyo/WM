package com.wm.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderLineModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer purchaseOrderLineId;
    private String purchaseOrderLineSku;
    @Column(precision = 10, scale = 2)
    private BigDecimal purchaseOrderLinePrice;
    @Column(precision = 10, scale = 2)
    private BigDecimal purchaseOrderLineVatRate;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrderModel purchaseOrder;


}
