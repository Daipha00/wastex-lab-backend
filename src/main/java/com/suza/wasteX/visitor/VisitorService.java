package com.suza.wasteX.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepo visitorRepo;

    public Visitor trackVisitor(String ipAddress) {
        // If visitor with same IP exists, update timestamp
        return visitorRepo.findByIpAddress(ipAddress)
                .map(visitor -> {
                    visitor.setTimestamp(LocalDateTime.now());
                    return visitorRepo.save(visitor);
                })
                .orElseGet(() -> {
                    // If not found, create new record
                    Visitor newVisitor = new Visitor(ipAddress, LocalDateTime.now());
                    return visitorRepo.save(newVisitor);
                });
    }

    public long getVisitorCount() {
        return visitorRepo.count();
    }
}
