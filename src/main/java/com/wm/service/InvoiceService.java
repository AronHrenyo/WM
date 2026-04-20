package com.wm.service;

import com.wm.entity.Invoice;
import com.wm.entity.InvoiceLine;
import com.wm.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepository repository;

    // ---------------- LIST ----------------
    public List<Invoice> findAll() {
        return repository.findAll();
    }

    public List<Invoice> findByDateBetween(LocalDate from, LocalDate to) {
        return repository.findByInvoiceDateBetween(from, to);
    }

    // ---------------- FIND ----------------
    public Invoice findById(Long id) {
        return repository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }

    // ---------------- CREATE ----------------
    public Invoice create(Invoice invoice) {

        repository.findByInvoiceNumber(invoice.getInvoiceNumber())
                .ifPresent(i -> {
                    throw new RuntimeException("Invoice number already exists");
                });

        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceStatus("NEW");

        invoice.setInvoiceNetSum(BigDecimal.ZERO);
        invoice.setInvoiceVatSum(BigDecimal.ZERO);
        invoice.setInvoiceGrossSum(BigDecimal.ZERO);

        return repository.save(invoice);
    }

    // ---------------- SAVE (EDIT) ----------------
    public void save(Invoice invoice) {

        BigDecimal net = BigDecimal.ZERO;
        BigDecimal vat = BigDecimal.ZERO;
        BigDecimal gross = BigDecimal.ZERO;

        if (invoice.getLines() != null) {
            for (InvoiceLine line : invoice.getLines()) {

                line.setInvoice(invoice);

                if (line.getInvoiceLineNetSum() != null)
                    net = net.add(line.getInvoiceLineNetSum());

                if (line.getInvoiceLineVatSum() != null)
                    vat = vat.add(line.getInvoiceLineVatSum());

                if (line.getInvoiceLineGrossSum() != null)
                    gross = gross.add(line.getInvoiceLineGrossSum());
            }
        }

        invoice.setInvoiceNetSum(net);
        invoice.setInvoiceVatSum(vat);
        invoice.setInvoiceGrossSum(gross);

        repository.save(invoice);
    }

    // ---------------- DELETE ----------------
    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}