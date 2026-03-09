package com.suza.wasteX.visitor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/visitors/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class VisitorController {

    private final VisitorService visitorService;

    // Track a visitor when endpoint is called
    @PostMapping
    public Visitor trackVisitor(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr(); // get client IP
        return visitorService.trackVisitor(ipAddress);
    }

    // Get total count of visitors
    @GetMapping
    public long getVisitorCount() {
        return visitorService.getVisitorCount();
    }
}
