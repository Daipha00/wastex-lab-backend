package com.suza.wasteX.project;

import com.suza.wasteX.audit.Auditable;
import com.suza.wasteX.partner.Partner;
import com.suza.wasteX.projectActivity.Activity;
import com.suza.wasteX.projectType.Type;
import com.suza.wasteX.sponsor.Sponsor;
import com.suza.wasteX.statuses.projectStatus.ProjectStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "project")
public class Project extends Auditable {
    @Id
    @SequenceGenerators(
            @SequenceGenerator(
                    name = "project_id_sequence",
                    sequenceName = "project_id_sequence"
            )
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "project_id")
    @NotNull
    private Long id;
    @Column(name = "project_name", nullable = false)
    @NotNull
    private String projectName;
    @Column(name = "project_budget", nullable = false)
    private double projectBudget;
    @ManyToMany
    @JoinTable(
            name = "project_sponsors",
            joinColumns = @JoinColumn(name = "project"),
            inverseJoinColumns = @JoinColumn(name = "sponsor")
    )
    private List<Sponsor> projectSponsor;
    @Size(min = 50, max = 1000)
    @Column(name = "project_description")
    private String description;
    @Column(name = "is_active")
    private boolean active;
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @Column(name = "end_date", nullable = false)
    private Date endDate;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Activity> activities;
    @OneToMany(mappedBy = "project")
    private List<Type> types;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectStatus> statuses;

}
