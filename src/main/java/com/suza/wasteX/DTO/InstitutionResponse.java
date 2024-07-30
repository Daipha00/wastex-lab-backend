package com.suza.wasteX.DTO;


import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionResponse {
    private Long id;
    private String name;
    private String location;

}