package com.wm.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductPackageWebController {

    @GetMapping("/product-package-create") //https://localhost:8443/product-package-create
    public String showProductPackageForm() {
        return "product/product-package-create"; //product-package-create.html
    }
}