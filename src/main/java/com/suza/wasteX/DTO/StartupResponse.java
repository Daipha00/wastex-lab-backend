package com.suza.wasteX.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartupResponse {
    private Long id;
    private String startup;
    private String website;
    private String phoneNumber;
    private String address;
    private Boolean active;
}
