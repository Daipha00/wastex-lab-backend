package com.suza.wasteX.wastex_site_api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String imgUrl;


}

