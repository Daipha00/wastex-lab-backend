package com.suza.wasteX.wastex_site_api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Carousel {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String imageUrl;
    private String title;
    private String subtitle;
    private String linkUrl;
    private Integer orderIndex;
}
