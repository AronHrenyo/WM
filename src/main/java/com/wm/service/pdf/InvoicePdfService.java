package com.wm.service.pdf;

import com.wm.entity.Invoice;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
public class InvoicePdfService extends AbstractPdfService<Invoice> {

    public InvoicePdfService(TemplateEngine templateEngine) {
        super(templateEngine);
    }

    public byte[] generateInvoicePdf(Invoice invoice) throws Exception {
        return generate("invoice/invoice-pdf", "invoice", invoice);
    }

}