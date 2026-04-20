package com.wm.repository;

import com.wm.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("""
        SELECT DISTINCT i FROM Invoice i
        LEFT JOIN FETCH i.partner
        LEFT JOIN FETCH i.lines l
        LEFT JOIN FETCH l.product
        WHERE i.invoiceId = :id
    """)
    Optional<Invoice> findByIdWithDetails(@Param("id") Long id);

    // Find by invoice number (for uniqueness check)
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    // Date filter (same as orders)
    List<Invoice> findByInvoiceDateBetween(LocalDate from, LocalDate to);
}