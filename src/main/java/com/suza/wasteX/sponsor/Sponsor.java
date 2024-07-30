package com.suza.wasteX.sponsor;

import com.suza.wasteX.project.Project;
import com.suza.wasteX.projectActivity.Activity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sponsor")
public class Sponsor {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "sponsor_id")
    private Long id;
    @Column(name = "sponsor_name")
    private String name;
    @ManyToMany(mappedBy = "projectSponsor")
    private List<Project> project;
    @ManyToMany(mappedBy = "activitySponsor")
    private List<Activity> activity;
}
