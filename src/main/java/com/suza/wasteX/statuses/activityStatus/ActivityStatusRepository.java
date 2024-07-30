package com.suza.wasteX.statuses.activityStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityStatusRepository extends JpaRepository<ActivityStatus, Long> {

    @Query("SELECT COUNT(s) FROM ActivityStatus s WHERE s.activity.id = :activityId")
    Long countActivityStatusByActivity(@Param("activityId") Long activityId);
}
