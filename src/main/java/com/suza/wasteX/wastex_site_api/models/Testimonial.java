package com.suza.wasteX.wastex_site_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "testimonial")
public class Testimonial {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    private Long id;
    private String firstName;
    private String lastName;
    private String title;
    private String description;
    private String imgUrl;
}
