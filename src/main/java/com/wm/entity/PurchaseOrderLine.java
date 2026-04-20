package com.wm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "purchase_order_line")
public class PurchaseOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseOrderLineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal purchaseOrderLinePrice;
    private Integer purchaseOrderLineVatKey;
    private Integer purchaseOrderLineQuantity;
    private BigDecimal purchaseOrderLineNetSum;
    private BigDecimal purchaseOrderLineVatSum;
    private BigDecimal purchaseOrderLineGrossSum;
}