package com.suza.wasteX.DTO.WebsiteDTO;

import lombok.Data;

@Data
public class CarouselResponse {
    private Long id;
    private String imageUrl;
    private String title;
    private String subtitle;
    private String linkUrl;
    private Integer orderIndex;
}
