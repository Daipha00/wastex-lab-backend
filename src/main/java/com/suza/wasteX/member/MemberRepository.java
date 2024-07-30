package com.suza.wasteX.member;

import com.suza.wasteX.projectActivity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query("SELECT m FROM Member m WHERE m.activity.id = :activityId")
    List<Member> findAllByActivityId(@Param("activityId") Long activityId);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.activity.id = :activityId")
    Long countTotalMembersByActivity(@Param("activityId") Long activityId);


    @Query("SELECT COUNT(m) FROM Member m WHERE m.activity.id = :activityId AND m.gender = 'Male'")
    long countMaleMembersByActivityId(@Param("activityId") Long activityId);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.activity.id = :activityId AND m.gender = 'Female'")
    long countFemaleMembersByActivityId(@Param("activityId") Long activityId);

    @Query("SELECT COUNT(m) FROM Member m")
    Long countTotalMembers();

    @Query("SELECT COUNT(m) FROM Member m WHERE m.gender = 'Male'")
    Long countMaleMembers();

    @Query("SELECT COUNT(m) FROM Member m WHERE m.gender = 'Female'")
    Long countFemaleMembers();
}
