package com.suza.wasteX.projectActivity;

import com.suza.wasteX.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("SELECT COUNT(a) FROM Activity a WHERE a.project.id = :projectId")
    Long countActivitiesByProjectId(@Param("projectId") Long projectId);
}
