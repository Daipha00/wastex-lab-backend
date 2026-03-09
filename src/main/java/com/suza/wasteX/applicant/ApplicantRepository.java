package com.suza.wasteX.applicant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    List<Applicant> findByProjectId(Long projectId);

    List<Applicant> findByEmail(String email);
}
