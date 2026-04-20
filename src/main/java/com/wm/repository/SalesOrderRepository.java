package com.wm.repository;

import com.wm.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    Optional<SalesOrder> findBySalesOrderNumber(String salesOrderNumber);

    List<SalesOrder> findBySalesOrderDateBetween(LocalDate from, LocalDate to);
}