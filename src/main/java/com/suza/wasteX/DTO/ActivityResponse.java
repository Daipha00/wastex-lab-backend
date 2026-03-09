package com.suza.wasteX.DTO;

import com.suza.wasteX.DTO.StatusDto.ActivityStatusResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponse {
    private Long id;
    private String projectActivityName;
    private String description;
    private double duration;
    private String unit;
    private Date startDate;
    private Date endDate;
    private List<SponsorResponse> activitySponsor;
    private List<ActivityStatusResponse> statuses;
}
