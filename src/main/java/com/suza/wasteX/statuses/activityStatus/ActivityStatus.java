package com.suza.wasteX.statuses.activityStatus;

import com.suza.wasteX.audit.Auditable;
import com.suza.wasteX.projectActivity.Activity;
import com.suza.wasteX.statuses.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "activity_status")
public class ActivityStatus extends Auditable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "status_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "activity_status_id", nullable = false)
    private Status status;
    @Column(name = "status_date")
    private LocalDate statusDate;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;
}
