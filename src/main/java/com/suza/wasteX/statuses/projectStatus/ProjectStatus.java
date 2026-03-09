package com.suza.wasteX.statuses.projectStatus;

import com.suza.wasteX.audit.Auditable;
import com.suza.wasteX.project.Project;
import com.suza.wasteX.statuses.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project_status")
public class ProjectStatus extends Auditable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "status_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "project_status_id", nullable = false)
    private Status status;
    @Column(name = "status_date")
    private Date statusDate;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
