package com.suza.wasteX.visitor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated ID for the Visitor entity

    private String ipAddress; // The IP address of the visitor
    private LocalDateTime timestamp; // The timestamp when the visitor visited

    // Constructor to initialize the IP address and timestamp
    public Visitor(String ipAddress, LocalDateTime timestamp) {
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
    }

    // Default constructor
    public Visitor() {
    }
}
