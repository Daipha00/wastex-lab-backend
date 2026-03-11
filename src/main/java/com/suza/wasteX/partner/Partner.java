package com.suza.wasteX.partner;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "partner")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_id")
    private Long id;

    @Column(name = "partner_name")
    private String name;

    @Column(name = "partner_email")
    private String email;

    @Column(name = "partner_phone")
    private String phone;

    @Column(name = "partner_image", columnDefinition = "TEXT")
    @Lob
    private String image;  // Store image as Base64 String
}
