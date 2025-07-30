package com.suza.wasteX.visitor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorRepo extends JpaRepository<Visitor, Long> {
    Optional<Visitor> findByIpAddress(String ipAddress);
}
