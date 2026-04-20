package com.wm.repository;

import com.wm.entity.ProductPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductPackageRepository extends JpaRepository<ProductPackage, Long> {

    Optional<ProductPackage> findByProductPackageBarcode(String productPackageBarcode);

}