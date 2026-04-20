package com.wm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseOrderId;

    private String purchaseOrderNumber;
    private LocalDate purchaseOrderDate;
    private String purchaseOrderStatus;

    private BigDecimal purchaseOrderNetSum;
    private BigDecimal purchaseOrderVatSum;
    private BigDecimal purchaseOrderGrossSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;
}