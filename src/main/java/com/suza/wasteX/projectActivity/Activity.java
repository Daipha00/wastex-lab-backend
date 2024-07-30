package com.suza.wasteX.projectActivity;

import com.suza.wasteX.audit.Auditable;
import com.suza.wasteX.member.Member;
import com.suza.wasteX.project.Project;
import com.suza.wasteX.sponsor.Sponsor;
import com.suza.wasteX.statuses.activityStatus.ActivityStatus;
import com.suza.wasteX.statuses.projectStatus.ProjectStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "activity")
public class Activity extends Auditable {
        @Id
        @SequenceGenerators(
                @SequenceGenerator(
                        name = "activity_id_sequence",
                        sequenceName = "activity_id_sequence"
                )
        )
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "activity_id")
        @NotNull
        private Long id;
        @Column(name = "activity_name", nullable = false)
        @NotNull
        @Size(min = 20, max = 50)
        private String projectActivityName;
        @Size(min = 50, max = 1000)
        @Column(name = "description")
        private String description;
        @Column(name = "duration", nullable = false)
        private double duration;
        @Column(name = "unit", nullable = false)
        private String unit;
        @Column(name = "start_date", nullable = false)
        private Date startDate;
        @Column(name = "end_date", nullable = false)
        private Date endDate;
        @ManyToOne
        @JoinColumn(name = "project")
        private Project project;
        @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ActivityStatus> statuses;
        @ManyToMany
        @JoinTable(
                name = "activity_sponsors",
                joinColumns = @JoinColumn(name = "activity"),
                inverseJoinColumns = @JoinColumn(name = "sponsor")
        )
        private List<Sponsor> activitySponsor;
        @OneToMany(mappedBy = "activity")
        private List<Member> members;


}
