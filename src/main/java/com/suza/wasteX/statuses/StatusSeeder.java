package com.suza.wasteX.statuses;

import com.suza.wasteX.statuses.projectStatus.StatusName;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatusSeeder {

    private final StatusRepository statusRepository;

    @PostConstruct
    public void seedStatuses() {
        for (StatusName name : StatusName.values()) {
            statusRepository.findByName(name)
                    .orElseGet(() -> {
                        Status status = new Status();
                        status.setName(name);
                        return statusRepository.save(status);
                    });
        }
    }
}
