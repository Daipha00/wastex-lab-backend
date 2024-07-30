package com.suza.wasteX.member;

import com.suza.wasteX.audit.Auditable;
import com.suza.wasteX.projectActivity.Activity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member")
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @NotNull
    private Long id;
    @Size(min = 3, max = 50)
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Size(min = 3, max = 50)
    @Column(name = "middle_name", nullable = false)
    @Size(min = 3, max = 50)
    private String middleName;
    @Column(name = "last_name", nullable = false)
    @Size(min = 3, max = 50)
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;
    @Column(name = "date_of_birth", nullable = false)
    private int age;
    //@Column(name="email",unique = true, nullable = false)
    @NotNull
    private String email;
    private String country;
    @Column(name = "organization",nullable = false)
    private String institution;
    @Column(name = "phone",nullable = false)
    private String phoneNumber;


    @ManyToOne
    @JoinColumn(name = "activity")
    private Activity activity;
}
