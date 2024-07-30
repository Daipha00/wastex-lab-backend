package com.suza.wasteX.statuses;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String name;
}
