package com.wm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sales_order")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesOrderId;

    private String salesOrderNumber;
    private LocalDate salesOrderDate;
    private String salesOrderStatus;

    private BigDecimal salesOrderNetSum;
    private BigDecimal salesOrderVatSum;
    private BigDecimal salesOrderGrossSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;
}