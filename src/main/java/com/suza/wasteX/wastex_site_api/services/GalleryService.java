package com.suza.wasteX.wastex_site_api.services;


import com.suza.wasteX.DTO.GalleryRequest;
import com.suza.wasteX.DTO.GalleryResponse;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.fileUploads.FileUploadService;
import com.suza.wasteX.wastex_site_api.models.Gallery;
import com.suza.wasteX.wastex_site_api.repositories.GalleryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GalleryService {

    private final ModelMapper modelMapper;
    private final FileUploadService fileUploadService;
    private final GalleryRepository galleryRepository;

    public GalleryResponse createGallery(String folderPath, GalleryRequest galleryRequest, MultipartFile file) throws Exception {
        Gallery gallery = modelMapper.map(galleryRequest, Gallery.class);
        String fileUrl = fileUploadService.uploadFile(folderPath, file);
        gallery.setImgUrl(fileUrl);
        gallery = galleryRepository.save(gallery);
        return mapToResponse(gallery);
    }

    public GalleryResponse getGalleryById(Long id) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gallery not found"));
        return mapToResponse(gallery);
    }

    public List<GalleryResponse> getAllGalleries() {
        return galleryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<GalleryResponse> getAllGalleryImagesFromFolder(String folder) {
        try {
            // List all galleries that have folder path in their imgUrl
            return galleryRepository.findAll()
                    .stream()
                    .filter(g -> g.getImgUrl() != null && g.getImgUrl().startsWith(folder))
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving gallery images from folder", e);
        }
    }

    public List<String> getAllGalleryImagesForBrowser() {
        return galleryRepository.findAll()
                .stream()
                .filter(g -> g.getImgUrl() != null) // ignore entries without an image
                .map(g -> fileUploadService.getFileUrl(g.getImgUrl())) // convert to presigned URL
                .toList();
    }


    public GalleryResponse updateGallery(Long id, GalleryRequest galleryRequest, String folderPath, MultipartFile file) throws Exception {
        Gallery existingGallery = galleryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gallery not found"));

        modelMapper.map(galleryRequest, existingGallery);
        if (file != null && !file.isEmpty()) {
            String fileUrl = fileUploadService.uploadFile(folderPath, file);
            existingGallery.setImgUrl(fileUrl);
        }

        existingGallery = galleryRepository.save(existingGallery);
        return mapToResponse(existingGallery);
    }

    public void deleteGallery(Long id) {
        galleryRepository.deleteById(id);
    }

    private GalleryResponse mapToResponse(Gallery gallery) {
        GalleryResponse response = modelMapper.map(gallery, GalleryResponse.class);
        response.setImgUrl(fileUploadService.getFileUrl(gallery.getImgUrl()));
        return response;
    }
}
