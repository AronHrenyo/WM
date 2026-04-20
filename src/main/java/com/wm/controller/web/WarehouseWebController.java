package com.wm.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WarehouseWebController {

    @GetMapping("/warehouse-map")
    public String warehousesMap() {
        return "warehouse/warehouse-map"; // Thymeleaf template
    }
}