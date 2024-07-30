package com.suza.wasteX.institution;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "institution")
public class Institution {
    @Id
    @SequenceGenerator(
            name = "institution_id_sequence",
            sequenceName = "institution_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "institution_id")
    private Long id;
    @Column(name = "institution_name")
    private String name;
    @Column(name = "location")
    private String location;
}
