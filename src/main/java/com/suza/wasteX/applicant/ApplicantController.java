package com.suza.wasteX.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applicants")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // allow Angular
public class ApplicantController {

    private final ApplicantService applicantService;

    // ==============================
    // 1. Apply for a project
    // ==============================
    @PostMapping("/project/{projectId}")
    public Applicant applyForProject(
            @PathVariable Long projectId,
            @RequestBody Applicant applicant
    ) {
        return applicantService.applyForProject(projectId, applicant);
    }

    // ==============================
    // 2. Get all applications by email (for logged-in user)
    // ==============================
    @GetMapping("/me")
    public List<Applicant> getMyApplications(@RequestParam String email) {
        return applicantService.getMyApplications(email);
    }

    // ==============================
    // 3. Get applicants by project (admin)
    // ==============================
    @GetMapping("/project/{projectId}")
    public List<Applicant> getApplicantsByProject(@PathVariable Long projectId) {
        return applicantService.getApplicantsByProject(projectId);
    }

    // ==============================
    // 4. Update applicant info
    // ==============================
    @PutMapping("/{id}")
    public Applicant updateApplicant(
            @PathVariable Long id,
            @RequestBody Applicant applicant
    ) {
        return applicantService.updateApplicant(id, applicant);
    }

    // ==============================
    // 5. Delete application
    // ==============================
    @DeleteMapping("/{id}")
    public void deleteApplicant(@PathVariable Long id) {
        applicantService.deleteApplicant(id);
    }

    // ==============================
    // 6. Update status (Admin)
    // ==============================
    @PutMapping("/{id}/status")
    public Applicant updateStatus(
            @PathVariable Long id,
            @RequestParam Applicant.Status status
    ) {
        return applicantService.updateStatus(id, status);
    }
}