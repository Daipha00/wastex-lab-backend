package com.suza.wasteX.wastex_site_api.services;

import com.suza.wasteX.DTO.WebsiteDTO.EventRequest;
import com.suza.wasteX.DTO.WebsiteDTO.EventResponse;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.fileUploads.FileUploadService;
import com.suza.wasteX.wastex_site_api.repositories.EventRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.suza.wasteX.wastex_site_api.models.Event;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private final ModelMapper modelMapper;
    private final FileUploadService fileUploadService;
    private final EventRepository eventRepository;


    public EventResponse createEvent(String folderPath, String eventRequest, MultipartFile file) throws Exception {
        Event event = modelMapper.map(eventRequest, Event.class);
        String fileUrl = fileUploadService.uploadFile(folderPath,file);
        event.setPictureUrl(fileUrl);
        event = eventRepository.save(event);
        return modelMapper.map(event, EventResponse.class);
    }

    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
        return modelMapper.map(event, EventResponse.class);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> modelMapper.map(event, EventResponse.class))
                .collect(Collectors.toList());
    }

    public EventResponse updateEvent(Long id, EventRequest eventRequest, String folderPath, MultipartFile file) throws Exception {
        Event existingEvent = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
        modelMapper.map(eventRequest, existingEvent);
        if (file != null && !file.isEmpty()) {
            String fileUrl = fileUploadService.uploadFile(folderPath,file);
            existingEvent.setPictureUrl(fileUrl);
        }
        existingEvent = eventRepository.save(existingEvent);
        return modelMapper.map(existingEvent, EventResponse.class);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

}
