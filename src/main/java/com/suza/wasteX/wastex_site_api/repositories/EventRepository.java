package com.suza.wasteX.wastex_site_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.suza.wasteX.wastex_site_api.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
