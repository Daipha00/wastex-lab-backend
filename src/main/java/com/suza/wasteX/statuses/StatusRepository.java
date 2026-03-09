package com.suza.wasteX.statuses;

import com.suza.wasteX.statuses.projectStatus.StatusName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(String name);
    Optional<Status> findByName(StatusName name);
}
