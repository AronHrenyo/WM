package com.wm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductPackageResponseDto {
    private String barcode;
    private Integer quantity;
    private BigDecimal discountPercentage;

    // Product adatok
    private String sku;
    private String productName;
    private String category;
    private Integer vatKey;
    private BigDecimal purchasePrice;
    private BigDecimal salesPrice;
}