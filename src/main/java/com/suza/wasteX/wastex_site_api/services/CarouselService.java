package com.suza.wasteX.wastex_site_api.services;

import com.suza.wasteX.DTO.WebsiteDTO.CarouselRequest;
import com.suza.wasteX.DTO.WebsiteDTO.CarouselResponse;
import com.suza.wasteX.fileUploads.FileUploadService;
import com.suza.wasteX.wastex_site_api.models.Carousel;
import com.suza.wasteX.wastex_site_api.repositories.CarouselRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarouselService {
    private final CarouselRepository carouselRepository;
    private final ModelMapper modelMapper;
    private final FileUploadService fileUploadService;

    public CarouselResponse createCarousel(CarouselRequest carouselRequest, String folder, MultipartFile file) throws Exception {
        Carousel carousel = modelMapper.map(carouselRequest, Carousel.class);
        if (file != null && !file.isEmpty()) {
            String fileUrl = fileUploadService.uploadFile(folder,file);
            carousel.setImageUrl(fileUrl);
        }
        // Automatically set the orderIndex
        Integer maxOrderIndex = carouselRepository.findMaxOrderIndex();
        carousel.setOrderIndex(maxOrderIndex != null ? maxOrderIndex + 1 : 0);
        carousel = carouselRepository.save(carousel);
        return mapToResponse(carousel);
    }

    public CarouselResponse getCarouselById(Long id) {
        Carousel carousel = carouselRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carousel item not found"));
        return mapToResponse(carousel);
    }

    public List<CarouselResponse> getAllCarousels() {
        return carouselRepository.findAllByOrderByOrderIndexAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public CarouselResponse updateCarousel(Long id, CarouselRequest carouselRequest, String folder, MultipartFile file) throws Exception {
        Carousel existingCarousel = carouselRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carousel item not found"));
        modelMapper.map(carouselRequest, existingCarousel);
        if (file != null && !file.isEmpty()) {
            String fileUrl = fileUploadService.uploadFile(folder,file);
            existingCarousel.setImageUrl(fileUrl);
        }
        existingCarousel = carouselRepository.save(existingCarousel);
        return mapToResponse(existingCarousel);
    }

    public void deleteCarousel(Long id) {
        carouselRepository.deleteById(id);
        // Reorder the remaining items
        List<Carousel> items = carouselRepository.findAllByOrderByOrderIndexAsc();
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setOrderIndex(i);
        }
        carouselRepository.saveAll(items);
    }

    private CarouselResponse mapToResponse(Carousel carouselItem) {
        CarouselResponse response = modelMapper.map(carouselItem, CarouselResponse.class);
        response.setImageUrl(fileUploadService.getFileUrl(carouselItem.getImageUrl()));
        return response;
    }
}