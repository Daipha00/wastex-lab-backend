package com.suza.wasteX.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartupRequest {
    private String startup;
    private String website;
    private String phoneNumber;
    private String address;
    private Boolean active;
}
