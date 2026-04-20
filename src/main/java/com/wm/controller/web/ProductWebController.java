package com.wm.controller.web;

import com.wm.entity.Product;
import com.wm.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductWebController {

    private final ProductRepository productRepository;

    public ProductWebController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // List products with optional name filter
    @GetMapping("/product-view") // https://localhost:8443/product-view
    public String listProducts(
            @RequestParam(value = "name", required = false) String name,
            Model model) {

        List<Product> products;

        if (name != null && !name.isEmpty()) {
            products = productRepository.findByProductNameContainingIgnoreCase(name);
        } else {
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("name", name);

        return "product/product-view"; // product-view.html
    }

    // Show the create form
    @GetMapping("/product-create") // https://localhost:8443/product-create
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/product-create"; // product-create.html
    }

    // Handle form submission
    @PostMapping("/product-create")
    public String createProduct(Product product) {
        productRepository.save(product);
        return "redirect:/product-view"; // redirect to product list
    }
}