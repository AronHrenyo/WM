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
@Table(name = "product_package")
public class ProductPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productPackageId;

    @Column(unique = true, nullable = false)
    private String productPackageBarcode;

    private Integer productPackageQuantity;

    private BigDecimal discountPercentage; // új mező

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Getter for Discount
    public BigDecimal getDiscountPercentage() {
        return discountPercentage != null ? discountPercentage : BigDecimal.ZERO;
    }

    // Calculated price
    public BigDecimal getPackageSalesPrice() {
        if (product == null || product.getProductSalesPrice() == null) return BigDecimal.ZERO;
        BigDecimal basePrice = product.getProductSalesPrice().multiply(BigDecimal.valueOf(productPackageQuantity));
        BigDecimal discount = basePrice.multiply(getDiscountPercentage()).divide(BigDecimal.valueOf(100));
        return basePrice.subtract(discount);
    }
}