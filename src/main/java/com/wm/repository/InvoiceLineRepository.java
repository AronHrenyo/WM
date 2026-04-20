package com.wm.repository;

import com.wm.entity.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {

    // Get all lines for a given invoice
    List<InvoiceLine> findByInvoice_InvoiceId(Long invoiceId);
}