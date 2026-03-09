package com.suza.wasteX.DTO;

import com.suza.wasteX.DTO.StatusDto.ProjectStatusRequest;
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
public class ProjectRequest {
    private String projectName;
    private double projectBudget;
    private List<String> projectSponsor;
    private String description;
    private boolean active;
    private Date startDate;
    private Date endDate;
    private String typeName;
    private List<ProjectStatusRequest> statuses;
}
