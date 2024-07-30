package com.suza.wasteX.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT COUNT(p) FROM Project p")
    long countTotalProjects();
}
