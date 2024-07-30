package com.suza.wasteX.DTO.WebsiteDTO;

import lombok.Data;

import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;

@Data
public class EventRequest {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String moreInfoUrl;
    private String pictureUrl;
}
