package com.wm.repository;

import com.wm.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    List<Partner> findByPartnerNameContainingIgnoreCase(String name);
}