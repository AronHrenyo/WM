package com.wm.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductPackageRequestDto {

    private String barcode;
    private Integer quantity;
    private BigDecimal discountPercentage;
    private Long productId;
}