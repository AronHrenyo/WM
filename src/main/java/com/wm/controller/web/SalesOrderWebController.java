package com.wm.controller.web;

import com.wm.entity.SalesOrder;
import com.wm.repository.PartnerRepository;
import com.wm.repository.SalesOrderRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SalesOrderWebController {

    private final SalesOrderRepository salesOrderRepository;
    private final PartnerRepository partnerRepository;

    public SalesOrderWebController(SalesOrderRepository salesOrderRepository,
                                   PartnerRepository partnerRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.partnerRepository = partnerRepository;
    }

    // List sales orders
    @GetMapping("/sales-order-view")
    public String listSalesOrders(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        List<SalesOrder> orders = (from != null && to != null) ?
                salesOrderRepository.findBySalesOrderDateBetween(from, to) :
                salesOrderRepository.findAll();

        model.addAttribute("orders", orders);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "sales-order/sales-order-view"; // Thymeleaf template
    }

    // Show create form
    @GetMapping("/sales-order-create")
    public String showCreateForm(Model model) {
        model.addAttribute("salesOrder", new SalesOrder());
        model.addAttribute("partners", partnerRepository.findAll());
        return "sales-order/sales-order-create";
    }

    // Handle form submission
    @PostMapping("/sales-order-create")
    public String createSalesOrder(SalesOrder salesOrder) {
        if (salesOrder.getPartner() == null) {
            throw new RuntimeException("Partner must be selected for a sales order");
        }
        salesOrderRepository.save(salesOrder);
        return "redirect:/sales-order/view";
    }
}