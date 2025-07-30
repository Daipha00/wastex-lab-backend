package com.suza.wasteX.partner;


import io.swagger.v3.oas.annotations.media.Content;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "partner")
public class Partner {
    @Id
    @SequenceGenerator(
            name = "partner_id_sequence",
            sequenceName = "partner_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "partner_id")
    private Long id;
    @Column(name = "partner_name")
    private String name;
    @Column(name = "partner_email")
    private String email;
    @Column(name = "partner_phone")
    private String phone;
    @Column(name = "partner_image", columnDefinition = "bytea")
    @Lob
    private byte[] image;

}
