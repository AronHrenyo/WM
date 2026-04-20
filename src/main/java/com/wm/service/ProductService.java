package com.wm.service;

import com.wm.entity.Product;
import com.wm.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository repository;

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product create(Product product) {

        // opcionális: SKU egyediség ellenőrzés
        repository.findByProductSku(product.getProductSku())
                .ifPresent(p -> {
                    throw new RuntimeException("SKU already exists: " + product.getProductSku());
                });

        return repository.save(product);
    }

    public Product update(Long id, Product product) {
        Product existing = findById(id);

        existing.setProductSku(product.getProductSku());
        existing.setProductName(product.getProductName());
        existing.setProductCategory(product.getProductCategory());
        existing.setProductPurchasePrice(product.getProductPurchasePrice());
        existing.setProductSalesPrice(product.getProductSalesPrice());
        existing.setProductVatKey(product.getProductVatKey());

        return repository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}