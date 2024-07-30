package com.suza.wasteX.wastex_site_api.services;

import com.suza.wasteX.DTO.WebsiteDTO.TestimonialResponse;
import com.suza.wasteX.DTO.WebsiteDTO.TestimonialRequest;
import com.suza.wasteX.DTO.WebsiteDTO.TestimonialResponse;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.fileUploads.FileUploadService;
import com.suza.wasteX.wastex_site_api.models.Testimonial;
import com.suza.wasteX.wastex_site_api.models.Testimonial;
import com.suza.wasteX.wastex_site_api.repositories.TestimonialRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TestimonialService {
        private final ModelMapper modelMapper;
        private final FileUploadService fileUploadService;
        private final TestimonialRepository testimonialRepository;


        public TestimonialResponse createTestimonial(String folderPath, TestimonialRequest testimonialRequest, MultipartFile file) throws Exception {
            Testimonial testimonial = modelMapper.map(testimonialRequest, Testimonial.class);
            String fileUrl = fileUploadService.uploadFile(folderPath,file);
            testimonial.setImgUrl(fileUrl);
            testimonial = testimonialRepository.save(testimonial);
            return mapToResponse(testimonial);
        }

        public TestimonialResponse getTestimonialById(Long id) {
            Testimonial testimonial = testimonialRepository.findById(id).orElseThrow(() -> new NotFoundException("Testimonial not found"));
            return mapToResponse(testimonial);
        }

        public List<TestimonialResponse> getAllTestimonials() {
            return testimonialRepository.findAll().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }

        public TestimonialResponse updateTestimonial(Long id, TestimonialRequest testimonialRequest, String folderPath, MultipartFile file) throws Exception {
            Testimonial existingTestimonial = testimonialRepository.findById(id).orElseThrow(() -> new NotFoundException("Testimonial not found"));
            modelMapper.map(testimonialRequest, existingTestimonial);
            if (file != null && !file.isEmpty()) {
                String fileUrl = fileUploadService.uploadFile(folderPath,file);
                existingTestimonial.setImgUrl(fileUrl);
            }
            existingTestimonial = testimonialRepository.save(existingTestimonial);
            return mapToResponse(existingTestimonial);
        }

        public void deleteTestimonial(Long id) {
            testimonialRepository.deleteById(id);
        }

    private TestimonialResponse mapToResponse(Testimonial testimonialItem) {
        TestimonialResponse response = modelMapper.map(testimonialItem, TestimonialResponse.class);
        response.setImgUrl(fileUploadService.getFileUrl(testimonialItem.getImgUrl()));
        return response;
    }
        
}
