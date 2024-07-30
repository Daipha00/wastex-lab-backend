package com.suza.wasteX.wastex_site_api.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suza.wasteX.DTO.WebsiteDTO.TestimonialRequest;
import com.suza.wasteX.DTO.WebsiteDTO.TestimonialResponse;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.wastex_site_api.services.TestimonialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/testimonials")
@RequiredArgsConstructor
@Tag(name = "WasteX Website API", description = "This is collection of API for managing WasteX Website")
public class TestimonialController {
        private final TestimonialService testimonialService;
        private final ObjectMapper objectMapper;
        @PostMapping(value = "/", consumes =  {MediaType.MULTIPART_FORM_DATA_VALUE})
        public ResponseEntity<TestimonialResponse> createTestimonial(@RequestParam("folder") String folderPath,
                                                                     @RequestPart("testimonial") String testimonialJson,
                                                                     @RequestPart("file") MultipartFile file) throws Exception {
            TestimonialRequest testimonialRequest = convertJsonToTestimonialRequest((testimonialJson));
            TestimonialResponse testimonialResponse = testimonialService.createTestimonial(folderPath,testimonialRequest,file);
            return ResponseEntity
                    .ok().body(testimonialResponse);

        }

        @GetMapping("/{id}")
        public ResponseEntity<TestimonialResponse> getTestimonialById(@PathVariable Long id) {
            TestimonialResponse testimonialResponse = testimonialService.getTestimonialById(id);
            return ResponseEntity.ok(testimonialResponse);
        }

        @GetMapping
        public ResponseEntity<List<TestimonialResponse>> getAllTestimonials() {
            List<TestimonialResponse> testimonials = testimonialService.getAllTestimonials();
            return ResponseEntity.ok(testimonials);
        }

        @PutMapping(value = "/{id}", consumes =  {MediaType.MULTIPART_FORM_DATA_VALUE})
        public ResponseEntity<TestimonialResponse> updateTestimonial(@PathVariable Long id,
                                                                     @RequestParam("folder") String folderPath,
                                                                     @RequestPart("testimonial") String testimonialJson,
                                                                     @RequestPart("file") MultipartFile file) throws Exception {
            TestimonialRequest testimonialRequest = convertJsonToTestimonialRequest(testimonialJson);
            TestimonialResponse testimonialResponse = testimonialService.updateTestimonial(id, testimonialRequest,folderPath, file);
            return ResponseEntity.ok(testimonialResponse);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteTestimonial(@PathVariable Long id) {
            testimonialService.deleteTestimonial(id);
            return ResponseEntity.noContent().build();
        }

        private TestimonialRequest convertJsonToTestimonialRequest(String testimonialJson) {
            try {
                return objectMapper.readValue(testimonialJson, TestimonialRequest.class);
            } catch (Exception e) {
                throw new NotFoundException("Error converting JSON to TestimonialRequest");
            }
        }
    
}
