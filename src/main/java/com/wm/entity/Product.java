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
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productSku;
    private String productName;
    private String productCategory;
    private BigDecimal productPurchasePrice;
    private BigDecimal productSalesPrice;
    private Integer productVatKey;
}