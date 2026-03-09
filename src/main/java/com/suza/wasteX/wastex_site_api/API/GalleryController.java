package com.suza.wasteX.wastex_site_api.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suza.wasteX.DTO.GalleryRequest;
import com.suza.wasteX.DTO.GalleryResponse;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.wastex_site_api.services.GalleryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/galleries")
@RequiredArgsConstructor
@Tag(name = "WasteX Website API", description = "This is a collection of APIs for managing WasteX Website Galleries")
public class GalleryController {

    private final GalleryService galleryService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GalleryResponse> createGallery(
            @RequestParam("folder") String folderPath,
            @RequestParam("title") String title,
            @RequestPart("file") MultipartFile file) throws Exception {

        // Build GalleryRequest from the form field 'title'
        com.suza.wasteX.DTO.GalleryRequest galleryRequest = new com.suza.wasteX.DTO.GalleryRequest();
        galleryRequest.setTitle(title);

        GalleryResponse galleryResponse = galleryService.createGallery(folderPath, galleryRequest, file);
        return ResponseEntity.ok(galleryResponse);
    }


    @GetMapping("/{id}")
    public ResponseEntity<GalleryResponse> getGalleryById(@PathVariable Long id) {
        GalleryResponse galleryResponse = galleryService.getGalleryById(id);
        return ResponseEntity.ok(galleryResponse);
    }

    @GetMapping
    public ResponseEntity<List<GalleryResponse>> getAllGalleries() {
        List<GalleryResponse> galleries = galleryService.getAllGalleries();
        return ResponseEntity.ok(galleries);
    }

    @GetMapping("/images")
    public ResponseEntity<List<String>> getAllImagesForBrowser() {
        List<String> imageUrls = galleryService.getAllGalleryImagesForBrowser();
        return ResponseEntity.ok(imageUrls);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GalleryResponse> updateGallery(@PathVariable Long id,
                                                         @RequestParam("folder") String folderPath,
                                                         @RequestPart("gallery") String galleryJson,
                                                         @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        GalleryRequest galleryRequest = convertJsonToGalleryRequest(galleryJson);
        GalleryResponse galleryResponse = galleryService.updateGallery(id, galleryRequest, folderPath, file);
        return ResponseEntity.ok(galleryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGallery(@PathVariable Long id) {
        galleryService.deleteGallery(id);
        return ResponseEntity.noContent().build();
    }

    private GalleryRequest convertJsonToGalleryRequest(String galleryJson) {
        try {
            return objectMapper.readValue(galleryJson, GalleryRequest.class);
        } catch (Exception e) {
            throw new NotFoundException("Error converting JSON to GalleryRequest");
        }
    }
}
