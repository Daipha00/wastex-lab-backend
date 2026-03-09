package com.suza.wasteX.OngoingEvent;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class OngoingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String startDate;
    private String endDate;

    @ElementCollection
    @CollectionTable(
            name = "event_highlights",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "highlight")
    private List<String> highlights;

    @ElementCollection
    @CollectionTable(
            name = "event_images",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "image_url")
    private List<String> images;
}
