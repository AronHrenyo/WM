package com.wm.controller.web;

import com.wm.entity.Invoice;
import com.wm.entity.Partner;
import com.wm.service.InvoiceService;
import com.wm.service.ProductService;
import com.wm.repository.PartnerRepository;
import com.wm.service.pdf.InvoicePdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InvoiceWebController {

    private final InvoiceService invoiceService;
    private final PartnerRepository partnerRepository;
    private final ProductService productService;
    private final InvoicePdfService invoicePdfService;

    // ---------------- LIST ----------------
    @GetMapping("/invoice-view") // https://localhost:8443/invoice-view
    public String listInvoices(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        List<Invoice> invoices;

        if (from != null && to != null) {
            invoices = invoiceService.findByDateBetween(from, to);
        } else {
            invoices = invoiceService.findAll();
        }

        model.addAttribute("orders", invoices);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "invoice/invoice-view";
    }

    // ---------------- CREATE ----------------
    @GetMapping("/invoice-create") // https://localhost:8443/invoice-create
    public String showCreateForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("partners", partnerRepository.findAll());
        return "invoice/invoice-create";
    }

    @PostMapping("/invoice-create")
    public String createInvoice(Invoice invoice) {

        if (invoice.getPartner() == null) {
            throw new RuntimeException("Partner must be selected");
        }

        invoiceService.create(invoice);

        return "redirect:/invoice-view";
    }

    // ---------------- EDIT ----------------
    @GetMapping("/invoice/{id}/edit")
    public String editInvoice(@PathVariable Long id, Model model) {

        Invoice invoice = invoiceService.findById(id);

        model.addAttribute("invoice", invoice);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("partners", partnerRepository.findAll());

        return "invoice/invoice-edit";
    }

    // ---------------- SAVE (EDIT) ----------------
    @PostMapping("/invoice/save")
    public String saveInvoice(@ModelAttribute Invoice invoice) {
        if(invoice.getPartner() != null && invoice.getPartner().getPartnerId() != null) {
            Partner partner = partnerRepository.findById(invoice.getPartner().getPartnerId())
                    .orElseThrow(() -> new RuntimeException("Partner not found"));
            invoice.setPartner(partner);
        } else {
            throw new RuntimeException("Partner is required");
        }

        invoiceService.save(invoice);
        return "redirect:/invoice-view";
    }

    // ---------------- PDF ----------------
    @GetMapping("/invoice/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long id) throws Exception {

        Invoice invoice = invoiceService.findById(id);

        byte[] pdf = invoicePdfService.generateInvoicePdf(invoice);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice-" + invoice.getInvoiceNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}