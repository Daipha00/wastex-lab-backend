package com.suza.wasteX.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembersCountResponse {
    private long totalMembers;
    private long maleMembers;
    private long femaleMembers;


    public MembersCountResponse(Long totalMembers, Long maleMembers, Long femaleMembers) {
        this.totalMembers = totalMembers;
        this.maleMembers = maleMembers;
        this.femaleMembers = femaleMembers;
    }

}
