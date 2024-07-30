package com.suza.wasteX.wastex_site_api.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suza.wasteX.DTO.WebsiteDTO.CarouselRequest;
import com.suza.wasteX.DTO.WebsiteDTO.CarouselResponse;
import com.suza.wasteX.wastex_site_api.services.CarouselService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carousel-items")
@Tag(name = "WasteX Website API", description = "This is collection of API for managing WasteX Website")
public class CarouselController {
    
    private final CarouselService carouselService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CarouselResponse> createCarousel(
            @RequestParam("folder") String folderPath,
            @RequestPart("carousel") String carouselJson,
            @RequestPart("file") MultipartFile file) throws Exception {
        CarouselRequest carouselRequest = convertJsonToCarouselRequest(carouselJson);
        CarouselResponse carouselResponse = carouselService.createCarousel(carouselRequest, folderPath,file);
        return ResponseEntity.ok(carouselResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarouselResponse> getCarouselById(@PathVariable Long id) {
        CarouselResponse carouselResponse = carouselService.getCarouselById(id);
        return ResponseEntity.ok(carouselResponse);
    }

    @GetMapping
    public ResponseEntity<List<CarouselResponse>> getAllCarousels() {
        List<CarouselResponse> carousels = carouselService.getAllCarousels();
        return ResponseEntity.ok(carousels);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CarouselResponse> updateCarousel(
            @PathVariable Long id,
            @RequestParam("folder") String folderPath,
            @RequestPart("carousel") String carouselJson,
            @RequestPart("file") MultipartFile file) throws Exception {
        CarouselRequest carouselRequest = convertJsonToCarouselRequest(carouselJson);
        CarouselResponse carouselResponse = carouselService.updateCarousel(id, carouselRequest,folderPath, file);
        return ResponseEntity.ok(carouselResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarousel(@PathVariable Long id) {
        carouselService.deleteCarousel(id);
        return ResponseEntity.noContent().build();
    }

    private CarouselRequest convertJsonToCarouselRequest(String carouselJson) {
        try {
            return objectMapper.readValue(carouselJson, CarouselRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to CarouselRequest", e);
        }
    }
}
