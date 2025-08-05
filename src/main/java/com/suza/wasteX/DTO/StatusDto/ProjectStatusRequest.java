package com.suza.wasteX.DTO.StatusDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStatusRequest {
    private Long id;
    private Long projectId;
    private Long statusId;
    private LocalDate statusDate;
}

