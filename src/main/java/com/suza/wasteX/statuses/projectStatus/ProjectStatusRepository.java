package com.suza.wasteX.statuses.projectStatus;

import com.suza.wasteX.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, Long> {
    Optional<ProjectStatus> findTopByProjectOrderByStatusDateDesc(Project project);
}
