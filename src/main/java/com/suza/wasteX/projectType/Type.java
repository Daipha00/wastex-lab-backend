package com.suza.wasteX.projectType;

import com.suza.wasteX.project.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project_type")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "type_id_sequence")
    @SequenceGenerator(
            name = "type_id_sequence",
            sequenceName = "type_id_sequence",
            allocationSize = 1
    )
    @Column(name = "project_type_id")
    private Long id;

    @Column(name = "project_type_name", unique = true)
    private String name;

//    @ManyToOne
//    @JoinColumn(name = "project_id")
//    private Project project;
}
