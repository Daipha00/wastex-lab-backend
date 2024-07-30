package com.suza.wasteX.startup;

import com.suza.wasteX.audit.Auditable;
import com.suza.wasteX.project.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "start_up")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Startup extends Auditable {
    @Id
    @SequenceGenerator(
            name = "startup_id_sequence",
            sequenceName = "startup_id_sequence"
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Long id;
    @Column(name = "startup_name", nullable = false)
    private String startup;
    private String website;
    @Size(min = 10, max = 10)
    private String phoneNumber;
    private String address;
    private boolean active;

}
