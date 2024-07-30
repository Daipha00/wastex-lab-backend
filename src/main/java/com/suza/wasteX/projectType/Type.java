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
    @SequenceGenerators(
            @SequenceGenerator(
                    name = "type_id_sequence",
                    sequenceName = "type_id_sequence"
            )
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "project_type_id")
    private Long id;
    @Column(name = "project_type_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "project")
    private Project project;
}
