package com.wm.service;

import com.wm.entity.Invoice;
import com.wm.entity.InvoiceLine;
import com.wm.repository.InvoiceLineRepository;
import com.wm.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceLineService {

    private final InvoiceLineRepository repository;
    private final InvoiceRepository invoiceRepository;

    // List all invoice lines
    public List<InvoiceLine> findAll() {
        return repository.findAll();
    }

    // Find invoice line by ID
    public InvoiceLine findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice line not found with id: " + id));
    }

    // Create new invoice line
    public InvoiceLine create(InvoiceLine line) {

        calculateLineSums(line);

        InvoiceLine saved = repository.save(line);

        updateInvoiceTotals(line.getInvoice());

        return saved;
    }

    // Update invoice line
    public InvoiceLine update(Long id, InvoiceLine line) {

        InvoiceLine existing = findById(id);

        existing.setProduct(line.getProduct());
        existing.setInvoiceLinePrice(line.getInvoiceLinePrice());
        existing.setInvoiceLineVatKey(line.getInvoiceLineVatKey());
        existing.setInvoiceLineQuantity(line.getInvoiceLineQuantity());

        calculateLineSums(existing);

        InvoiceLine saved = repository.save(existing);

        updateInvoiceTotals(existing.getInvoice());

        return saved;
    }

    // Delete invoice line
    public void delete(Long id) {

        InvoiceLine line = findById(id);
        Invoice invoice = line.getInvoice();

        repository.delete(line);

        updateInvoiceTotals(invoice);
    }

    // 🔹 Helper methods

    public void calculateLineSums(InvoiceLine line) {

        BigDecimal qty = BigDecimal.valueOf(line.getInvoiceLineQuantity());
        BigDecimal price = line.getInvoiceLinePrice();

        BigDecimal net = price.multiply(qty);

        BigDecimal vat = net.multiply(BigDecimal.valueOf(line.getInvoiceLineVatKey()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal gross = net.add(vat);

        line.setInvoiceLineNetSum(net);
        line.setInvoiceLineVatSum(vat);
        line.setInvoiceLineGrossSum(gross);
    }

    private void updateInvoiceTotals(Invoice invoice) {

        List<InvoiceLine> lines =
                repository.findByInvoice_InvoiceId(invoice.getInvoiceId());

        BigDecimal netTotal = lines.stream()
                .map(InvoiceLine::getInvoiceLineNetSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal vatTotal = lines.stream()
                .map(InvoiceLine::getInvoiceLineVatSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal grossTotal = lines.stream()
                .map(InvoiceLine::getInvoiceLineGrossSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        invoice.setInvoiceNetSum(netTotal);
        invoice.setInvoiceVatSum(vatTotal);
        invoice.setInvoiceGrossSum(grossTotal);

        invoiceRepository.save(invoice);
    }
}