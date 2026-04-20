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
@Table(name = "sales_order_line")
public class SalesOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesOrderLineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id", nullable = false)
    private SalesOrder salesOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal salesOrderLinePrice;
    private Integer salesOrderLineVatKey;
    private Integer salesOrderLineQuantity;
    private BigDecimal salesOrderLineNetSum;
    private BigDecimal salesOrderLineVatSum;
    private BigDecimal salesOrderLineGrossSum;
}