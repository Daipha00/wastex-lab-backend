package com.suza.wasteX.DTO.WebsiteDTO;

import lombok.Data;

import java.util.List;

@Data
public class OngoingEventResponse {

    private Long id;
    private String title;
    private String startDate;
    private String endDate;
    private List<String> highlights;
    private List<String> images;
}
