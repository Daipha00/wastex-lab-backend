package com.suza.wasteX.partner;

import com.suza.wasteX.project.Project;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "partner")
public class Partner {
    @Id
    @SequenceGenerator(
            name = "partner_id_sequence",
            sequenceName = "partner_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "partner_id")
    private Long id;
    @Column(name = "partner_name")
    private String name;
    @Column(name = "partner_email")
    private String email;
    @Column(name = "partner_phone")
    private String phone;
}
