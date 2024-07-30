package com.suza.wasteX.wastex_site_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String moreInfoUrl;
    private String pictureUrl;
}
