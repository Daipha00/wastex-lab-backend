package com.suza.wasteX.DTO;

import com.suza.wasteX.DTO.StatusDto.ActivityStatusRequest;
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
public class ActivityRequest {
    private String projectActivityName;
    private List<Long> activitySponsor;
    private String description;
    private double duration;
    private String unit;
    private Date startDate;
    private Date endDate;
    private List<ActivityStatusRequest> statuses;
}
