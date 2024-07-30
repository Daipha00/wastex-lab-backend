package com.suza.wasteX.statuses.projectStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, Long> {
}
