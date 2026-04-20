package com.wm.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductPackageCreateRequestDto {
    private String sku;
    private String productName;
    private String category;
    private Integer vatKey;
    private BigDecimal purchasePrice;
    private BigDecimal salesPrice;
    private Integer quantity;
    private String barcode;

    private BigDecimal discountPercentage;
}