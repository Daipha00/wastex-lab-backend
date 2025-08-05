package com.suza.wasteX.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Partner> addPartner(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam MultipartFile image
    ) throws IOException {
        Partner savedPartner = partnerService.savePartner(name, email, phone, image);
        return new ResponseEntity<>(savedPartner, HttpStatus.CREATED);
    }

    @GetMapping("/images")
    public ResponseEntity<List<String>> getAllImages() {
        List<String> images = partnerService.getAllPartnerImages();
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Partner>> getAllPartners() {
        return ResponseEntity.ok(partnerService.getAllPartners());
    }
}
