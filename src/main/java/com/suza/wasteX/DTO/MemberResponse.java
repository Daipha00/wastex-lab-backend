package com.suza.wasteX.DTO;

import com.suza.wasteX.member.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Gender gender;
    private int age;
    private String email;
    private String country;
    private String institution;
    private String phoneNumber;
}
