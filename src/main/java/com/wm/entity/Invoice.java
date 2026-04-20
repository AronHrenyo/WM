package com.wm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private String invoiceStatus;

    private BigDecimal invoiceNetSum;
    private BigDecimal invoiceVatSum;
    private BigDecimal invoiceGrossSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<InvoiceLine> lines;


    //invoice-pdf.html
    public List<InvoiceLine> getInvoiceLines() {
        return lines;
    }
}

