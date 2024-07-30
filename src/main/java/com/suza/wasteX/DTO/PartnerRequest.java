package com.suza.wasteX.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerRequest {
    private String name;
    private String email;
    private String phone;
}
