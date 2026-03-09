package com.suza.wasteX.OngoingEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OngoingEventRepo extends JpaRepository<OngoingEvent, Long> {
}
