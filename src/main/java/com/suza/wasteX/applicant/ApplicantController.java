//package com.suza.wasteX.applicant;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/applicants")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
//public class ApplicantController {
//
//    private final ApplicantService applicantService;
//
//    @PostMapping("/project/{projectId}")
//    public Applicant applyForProject(
//            @PathVariable Long projectId,
//            @RequestBody Applicant applicant
//    ) {
//        return applicantService.applyForProject(projectId, applicant);
//    }
//
//    @GetMapping("/me")
//    public List<Applicant> getMyApplications(@RequestParam String email) {
//        return applicantService.getMyApplications(email);
//    }
//
//    @GetMapping("/project/{projectId}")
//    public List<Applicant> getApplicantsByProject(@PathVariable Long projectId) {
//        return applicantService.getApplicantsByProject(projectId);
//    }
//
//
//    @PutMapping("/{id}")
//    public Applicant updateApplicant(
//            @PathVariable Long id,
//            @RequestBody Applicant applicant
//    ) {
//        return applicantService.updateApplicant(id, applicant);
//    }
//
//
//    @DeleteMapping("/{id}")
//    public void deleteApplicant(@PathVariable Long id) {
//        applicantService.deleteApplicant(id);
//    }
//
//
//    @PutMapping("/{id}/status")
//    public Applicant updateStatus(
//            @PathVariable Long id,
//            @RequestParam Applicant.Status status
//    ) {
//        return applicantService.updateStatus(id, status);
//    }
//}