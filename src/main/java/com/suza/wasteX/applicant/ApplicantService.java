package com.suza.wasteX.applicant;

import com.suza.wasteX.project.Project;
import com.suza.wasteX.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    // Apply for project
    public Applicant applyForProject(Long projectId, Applicant applicant) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        applicant.setProject(project);
        applicant.setStatus(Applicant.Status.PENDING);

        return applicantRepository.save(applicant);
    }

    // Get all applications for a project (admin)
    public List<Applicant> getApplicantsByProject(Long projectId) {
        return applicantRepository.findByProjectId(projectId);
    }

    // Get applications for an applicant (by email)
    public List<Applicant> getMyApplications(String email) {
        return applicantRepository.findByEmail(email);
    }

    // Update applicant info (only self)
    public Applicant updateApplicant(Long applicantId, Applicant updatedApplicant) {
        Applicant existing = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        existing.setFirstName(updatedApplicant.getFirstName());
        existing.setMiddleName(updatedApplicant.getMiddleName());
        existing.setLastName(updatedApplicant.getLastName());
        existing.setPhoneNumber(updatedApplicant.getPhoneNumber());

        return applicantRepository.save(existing);
    }

    // Delete application (self)
    public void deleteApplicant(Long applicantId) {
        applicantRepository.deleteById(applicantId);
    }

    // Admin updates status
    public Applicant updateStatus(Long applicantId, Applicant.Status status) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));
        applicant.setStatus(status);
        return applicantRepository.save(applicant);
    }
}