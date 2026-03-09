package com.suza.wasteX.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;


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

    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPartnerImage(@PathVariable Long id) throws IOException {
        byte[] imageBytes = partnerService.getPartnerImage(id);
        return ResponseEntity.ok(imageBytes);
    }

    // UPDATE
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Partner> updatePartner(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {
        Partner updatedPartner = partnerService.updatePartner(id, name, email, phone, image);
        return ResponseEntity.ok(updatedPartner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();
    }
}
