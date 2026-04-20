package com.wm.controller.rest;

import com.wm.dto.ProductPackageCreateRequestDto;
import com.wm.dto.ProductPackageRequestDto;
import com.wm.dto.ProductPackageResponseDto;
import com.wm.entity.Product;
import com.wm.entity.ProductPackage;
import com.wm.repository.ProductRepository;
import com.wm.service.ProductPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/product-packages")
@RequiredArgsConstructor
public class ProductPackageRestController {

    private final ProductPackageService productPackageService;
    private final ProductRepository productRepository;

    @GetMapping("/by-barcode/{barcode}")
    public ProductPackageResponseDto getByBarcode(@PathVariable String barcode) {
        return productPackageService.findByBarcode(barcode)
                .map(pkg -> {
                    ProductPackageResponseDto dto = new ProductPackageResponseDto();
                    dto.setBarcode(pkg.getProductPackageBarcode());
                    dto.setQuantity(pkg.getProductPackageQuantity());
                    dto.setDiscountPercentage(pkg.getDiscountPercentage());

                    // Product mezők
                    if (pkg.getProduct() != null) {
                        dto.setSku(pkg.getProduct().getProductSku());
                        dto.setProductName(pkg.getProduct().getProductName());
                        dto.setCategory(pkg.getProduct().getProductCategory());
                        dto.setVatKey(pkg.getProduct().getProductVatKey());
                        dto.setPurchasePrice(pkg.getProduct().getProductPurchasePrice());
                        dto.setSalesPrice(pkg.getProduct().getProductSalesPrice());
                    }
                    return dto;
                })
                .orElse(null);
    }

    @PostMapping
    public ProductPackage save(@RequestBody ProductPackageRequestDto request) {

        if (request.getProductId() == null) {
            throw new IllegalArgumentException("productId must not be null");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Új logika: salesPrice/purchasePrice helyett discountPercentage
        return productPackageService.save(
                request.getBarcode(),
                request.getQuantity(),
                request.getDiscountPercentage(), // DTO-ból kell ezt a mezőt
                product
        );
    }

    @PostMapping("/create-product-package")
    public ProductPackage createProductPackage(@RequestBody ProductPackageCreateRequestDto request) {

        Product product = Product.builder()
                .productSku(request.getSku())
                .productName(request.getProductName())
                .productCategory(request.getCategory())
                .productVatKey(request.getVatKey())
                .productPurchasePrice(request.getPurchasePrice())
                .productSalesPrice(request.getSalesPrice())
                .build();
        product = productRepository.save(product);

        return productPackageService.save(
                request.getBarcode(),
                request.getQuantity(),
                request.getDiscountPercentage(), // új mező
                product
        );
    }
}