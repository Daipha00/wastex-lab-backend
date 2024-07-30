package com.suza.wasteX.wastex_site_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Feature {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String title;
    private String description;
}
