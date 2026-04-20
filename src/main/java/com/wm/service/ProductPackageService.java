package com.wm.service;

import com.wm.entity.Product;
import com.wm.entity.ProductPackage;
import com.wm.repository.ProductPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductPackageService {

    private final ProductPackageRepository repository;

    public ProductPackage save(String barcode,
                               Integer quantity,
                               BigDecimal discountPercentage,
                               Product product) {

        Optional<ProductPackage> existing = repository.findByProductPackageBarcode(barcode);

        if (existing.isPresent()) {
            // update
            ProductPackage pkg = existing.get();
            pkg.setProductPackageQuantity(quantity);
            pkg.setDiscountPercentage(discountPercentage);
            pkg.setProduct(product);

            return repository.save(pkg);
        } else {
            // create
            ProductPackage pkg = ProductPackage.builder()
                    .productPackageBarcode(barcode)
                    .productPackageQuantity(quantity)
                    .discountPercentage(discountPercentage)
                    .product(product)
                    .build();

            return repository.save(pkg);
        }
    }

    public Optional<ProductPackage> findByBarcode(String barcode) {
        return repository.findByProductPackageBarcode(barcode);
    }
}