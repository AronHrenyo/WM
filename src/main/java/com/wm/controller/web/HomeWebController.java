package com.wm.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeWebController {

    @GetMapping("/home") // https://localhost:8443/home
    public String home() {
        return "home"; // resources/templates/home.html
    }

}