package com.suza.wasteX.visitor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class VisitorCount {

    @Id
    private Long id = 1L; // Use a constant ID (1L) for tracking total visitors

    private Integer count; // The count of unique visitors

    public VisitorCount() {
    }

    public VisitorCount(int count) {
        this.count = count;
    }
}
