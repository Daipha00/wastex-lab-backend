package com.suza.wasteX.skill;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "skill")
public class Skill {
    @Id
    @SequenceGenerator(
            name = "skill_id_sequence",
            sequenceName = "skill_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "skill_id")
    private Long id;
    @Column(name = "skill_title")
    private String title;
}
