package com.suza.wasteX.wastex_site_api.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suza.wasteX.DTO.WebsiteDTO.EventRequest;
import com.suza.wasteX.DTO.WebsiteDTO.EventResponse;
import com.suza.wasteX.customException.NotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.suza.wasteX.wastex_site_api.services.EventService;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.util.List;

@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
@Tag(name = "WasteX Website API", description = "This is collection of API for managing WasteX Website")
public class EventController {
    private final EventService eventService;
    private final ObjectMapper objectMapper;
    @PostMapping(value = "/", consumes =  {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EventResponse> createEvent(@RequestParam("folder") String folderPath,
                                                     @RequestPart("event") String eventJson,
                                                     @RequestPart("file") MultipartFile file) throws Exception {
//        EventRequest eventRequest = convertJsonToEventRequest(eventJson);
        EventResponse eventResponse = eventService.createEvent(folderPath,eventJson,file);
        return ResponseEntity
                .ok().body(eventResponse);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        EventResponse eventResponse = eventService.getEventById(id);
        return ResponseEntity.ok(eventResponse);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PutMapping(value = "/{id}", consumes =  {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id,
                                                     @RequestParam("folder") String folderPath,
                                                     @RequestPart("event") String eventJson,
                                                     @RequestPart("file") MultipartFile file) throws Exception {
        EventRequest eventRequest = convertJsonToEventRequest(String.valueOf(eventJson));
        EventResponse eventResponse = eventService.updateEvent(id, eventRequest,folderPath, file);
        return ResponseEntity.ok(eventResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    private EventRequest convertJsonToEventRequest(String eventJson) {
        try {
            return objectMapper.readValue(eventJson, EventRequest.class);
        } catch (Exception e) {
            throw new NotFoundException("Error converting JSON to EventRequest");
        }
    }
}
