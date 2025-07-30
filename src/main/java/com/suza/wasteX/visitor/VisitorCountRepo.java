package com.suza.wasteX.visitor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorCountRepo extends JpaRepository<VisitorCount, Long> {
}
