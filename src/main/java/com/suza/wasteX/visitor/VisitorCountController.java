package com.suza.wasteX.visitor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visitor")
@CrossOrigin("*")
public class VisitorCountController {

    @Autowired
    private VisitorCountService visitorCountService;

    // API to increment the visitor count and return the updated count
    @GetMapping("/increment")
    public int incrementVisitor(HttpServletRequest request) {
        return visitorCountService.incrementVisitorCount(request);
    }

    // API to get the current total number of unique visitors
    @GetMapping("/count")
    public int getVisitorCount() {
        return visitorCountService.getVisitorCount();
    }
}
