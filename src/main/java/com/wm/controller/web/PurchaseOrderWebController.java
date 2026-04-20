package com.wm.controller.web;

import com.wm.entity.Partner;
import com.wm.entity.PurchaseOrder;
import com.wm.repository.PartnerRepository;
import com.wm.repository.PurchaseOrderRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class PurchaseOrderWebController {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PartnerRepository partnerRepository;

    public PurchaseOrderWebController(PurchaseOrderRepository purchaseOrderRepository,
                                      PartnerRepository partnerRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.partnerRepository = partnerRepository;
    }

    // List purchase orders with optional date filter
    @GetMapping("/purchase-order-view") // https://localhost:8443/purchase-order-view
    public String listPurchaseOrders(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        List<PurchaseOrder> orders;
        if (from != null && to != null) {
            orders = purchaseOrderRepository.findByPurchaseOrderDateBetween(from, to);
        } else {
            orders = purchaseOrderRepository.findAll();
        }

        model.addAttribute("orders", orders);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "purchase-order/purchase-order-view"; // purchase-order-view.html
    }

    // Show the create form
    @GetMapping("/purchase-order-create") // https://localhost:8443/purchase-order-create
    public String showCreateForm(Model model) {
        model.addAttribute("purchaseOrder", new PurchaseOrder());

        // Pass the list of partners for the select dropdown
        List<Partner> partners = partnerRepository.findAll();
        model.addAttribute("partners", partners);

        return "purchase-order/purchase-order-create"; // purchase-order-create.html
    }

    // Handle form submission
    @PostMapping("/purchase-order-create")
    public String createPurchaseOrder(PurchaseOrder purchaseOrder) {
        // Ensure a partner is selected
        if (purchaseOrder.getPartner() == null) {
            throw new RuntimeException("Partner must be selected for a purchase order");
        }

        purchaseOrderRepository.save(purchaseOrder);
        return "redirect:/purchase-order-view"; // redirect to purchase-order-view.html
    }
}