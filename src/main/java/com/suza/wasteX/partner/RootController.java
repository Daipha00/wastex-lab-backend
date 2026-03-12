package com.suza.wasteX.partner;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String home() {
        return "Welcome to WasteX Backend!";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";  // hii endpoint ni safi kwa Render health check
    }
}