package com.suza.wasteX.DTO;

import com.suza.wasteX.DTO.StatusDto.ProjectStatusResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String projectName;
    private double projectBudget;
    private List<SponsorResponse> projectSponsor;
    private String description;
    private boolean active;
    private Date startDate;
    private Date endDate;
    private TypeResponse type;
    private List<ActivityResponse> activities;
    private List<ProjectStatusResponse> statuses;
}
