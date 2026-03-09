package com.suza.wasteX.project;

import com.suza.wasteX.DTO.*;
import java.util.ArrayList;
import com.suza.wasteX.DTO.StatusDto.ProjectStatusRequest;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.projectActivity.Activity;
import com.suza.wasteX.projectActivity.ActivityRepository;
import com.suza.wasteX.projectType.Type;
import com.suza.wasteX.projectType.TypeRepository;
import com.suza.wasteX.sponsor.Sponsor;
import com.suza.wasteX.sponsor.SponsorRepository;
import com.suza.wasteX.statuses.Status;
import com.suza.wasteX.statuses.StatusRepository;
import com.suza.wasteX.statuses.activityStatus.ActivityStatus;
import com.suza.wasteX.statuses.projectStatus.ProjectStatus;
import com.suza.wasteX.statuses.projectStatus.ProjectStatusRepository;
import com.suza.wasteX.statuses.projectStatus.StatusName;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Builder
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectStatusRepository projectStatusRepository;
    private final StatusRepository statusRepository;
    private final ActivityRepository activityRepository;
    private final SponsorRepository sponsorRepository;
    private final ModelMapper modelMapper;
    private final TypeRepository typeRepository;

    @Transactional
   public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
      return projects
               .stream().map(project -> modelMapper.map(project, ProjectResponse.class))
               .collect(Collectors.toList());
   }



    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        Project project = modelMapper.map(request, Project.class);

        // --- Handle Sponsors ---
        List<Sponsor> sponsors = Optional.ofNullable(request.getProjectSponsor())
                .orElse(List.of())
                .stream()
                .map(name -> sponsorRepository.findByName(name)
                        .orElseThrow(() -> {
                            log.info("Sponsor with name {} is not found", name);
                            return new NotFoundException("Sponsor name is not found");
                        }))
                .toList();
        project.setProjectSponsor(sponsors);

        // --- Handle Status ---
        List<ProjectStatus> statuses;

        if (request.getStatuses() != null && !request.getStatuses().isEmpty()) {
            Project finalProject = project;
            statuses = request.getStatuses().stream()
                    .map(statusRequest -> {
                        ProjectStatus status = new ProjectStatus();
                        Status statusEntity = statusRepository.findById(statusRequest.getId())
                                .orElseThrow(() -> {
                                    log.info("Status with id {} is not found", statusRequest.getId());
                                    return new NotFoundException("Invalid status ID");
                                });
                        status.setStatus(statusEntity);
                        status.setStatusDate(statusRequest.getStatusDate());
                        status.setProject(finalProject);
                        return status;
                    })
                    .collect(Collectors.toList());
        } else {
            // Default status as PENDING
            Status defaultStatus = statusRepository.findByName(StatusName.PENDING)
                    .orElseThrow(() -> new NotFoundException("Default 'Pending' status not found"));

            ProjectStatus defaultProjectStatus = new ProjectStatus();
            defaultProjectStatus.setStatus(defaultStatus);
            defaultProjectStatus.setStatusDate(request.getStartDate());
            defaultProjectStatus.setProject(project);

            statuses = List.of(defaultProjectStatus);
        }
        project.setStatuses(statuses);

        // --- Handle Type ---
        if (request.getTypeName() != null && !request.getTypeName().isBlank()) {
            Type type = typeRepository.findByName(request.getTypeName())
                    .orElseThrow(() -> new NotFoundException("Type not found: " + request.getTypeName()));

            project.setType(type); // ✅ assign type to project
        }


        // --- Save Project ---
        project = projectRepository.save(project);

        return modelMapper.map(project, ProjectResponse.class);
    }




    @Transactional
    public ProjectResponse getProjectById(Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);

        if (projectOptional.isEmpty()) {
            log.info("status with id {} doesn't exists", projectOptional);
            throw new NotFoundException("Project id is not found");
        }

        ProjectResponse response = modelMapper.map(projectOptional.get(), ProjectResponse.class);
        response.setType(response.getType());
        response.setActivities(response.getActivities().stream().toList());
        response.setStatuses(response.getStatuses().stream().toList());
        return response;
    }
  @Transactional
  public boolean deleteProject(Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
      if(projectOptional.isPresent()) {
          projectRepository.delete(projectOptional.get());
          return true;
      }
      return false;
  }


//  business logics
    @Transactional
    public ProjectResponse addProjectStatus(Long projectId, ProjectStatusRequest statusRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.info("Project with id {} is not found ", projectId);
                    return new NotFoundException("Invalid project ID");
                });

        ProjectStatus newStatus = new ProjectStatus();
        Status statusEntity = statusRepository.findById(statusRequest.getId())
                .orElseThrow(() -> {
                    log.info("project status with id {} is not found", statusRequest.getId());
                    return new NotFoundException("Invalid status ID");
                });
        newStatus.setStatus(statusEntity);
        newStatus.setStatusDate(statusRequest.getStatusDate());
        newStatus.setProject(project);
        project.getStatuses().add(newStatus);

        project = projectRepository.save(project);
        return modelMapper.map(project, ProjectResponse.class);
    }

    @Transactional
    public ProjectResponse updateProjectSponsors(Long projectId, List<String> sponsorNames) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.info("Project with id {} is not found", projectId);
                    return new NoResultException("Project id is not found");
                });

        List<Sponsor> sponsors = Optional.ofNullable(sponsorNames)
                .orElse(List.of())
                .stream()
                .map(name -> sponsorRepository.findByName(name)
                        .orElseThrow(() -> {
                            log.info("Sponsor with name {} is not found", name);
                            return new NotFoundException("Sponsor not found: " + name);
                        }))
                .collect(Collectors.toList());

        project.setProjectSponsor(sponsors);

        Project updatedProject = projectRepository.save(project);

        return modelMapper.map(updatedProject, ProjectResponse.class);
    }

    @Transactional
    public ProjectResponse addActivityToProject(Long projectId, ActivityRequest request) {
        Project project  = projectRepository.findById(projectId)
                .orElseThrow(()-> {
                    log.info("Sponsor with id {} is not found", projectId);
                    return new NotFoundException("Sponsor id is not found");
                });
        List<Sponsor> sponsors = request.getActivitySponsor()
                .stream()
                .map(id -> sponsorRepository.findById(id).orElseThrow(() -> {
                    log.info("Sponsor with id {} is not found", id);
                    return new NotFoundException("Sponsor id is not found");
                })).toList();

        if (request.getStatuses() != null) {
            List<ActivityStatus> statuses = request.getStatuses().stream()
                    .map(statusRequest -> {
                        ActivityStatus status = new ActivityStatus();
                        Status statusEntity = statusRepository.findById(statusRequest.getId())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid status ID"));
                        status.setStatus(statusEntity);
                        status.setStatusDate(statusRequest.getStatusDate());
                        return status;
                    })
                    .collect(Collectors.toList());
            Activity newActivity = modelMapper.map(request, Activity.class);
            newActivity.setStatuses(statuses);
            newActivity.setActivitySponsor(sponsors);
            newActivity.setProject(project);
            project.getActivities().add(newActivity);
        }


            projectRepository.save(project);
            return modelMapper.map(project, ProjectResponse.class);

    }

    @Transactional
    public ProjectResponse addSponsorsToProject(Long projectId, List<Long> sponsorIds) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));

        List<Sponsor> currentSponsors = project.getProjectSponsor();
        List<Sponsor> newSponsors = sponsorIds.stream()
                .map(id -> sponsorRepository.findById(id).orElseThrow(() -> new RuntimeException("Sponsor not found")))
                .filter(newSponsor -> !currentSponsors.contains(newSponsor))
                .toList();
        currentSponsors.addAll(newSponsors);
        project.setProjectSponsor(currentSponsors);
        project = projectRepository.save(project);
        return modelMapper.map(project, ProjectResponse.class);
    }

//    project API statistics
public Long countTotalProjects() {
    return projectRepository.countTotalProjects();
}
    public long countActivitiesByProjectId(Long projectId) {
        return activityRepository.countActivitiesByProjectId(projectId);
    }

    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        // Fetch the existing project
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Project with id {} is not found", id);
                    return new NotFoundException("Project id is not found");
                });

        // Update basic project fields
        project.setProjectName(request.getProjectName());
        project.setProjectBudget(request.getProjectBudget());
        project.setDescription(request.getDescription());
        project.setActive(request.isActive());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        if (request.getProjectSponsor() != null && !request.getProjectSponsor().isEmpty()) {
            List<Sponsor> sponsors = request.getProjectSponsor().stream()
                    .map(name -> sponsorRepository.findByName(name)
                            .orElseThrow(() -> {
                                log.info("Sponsor with name {} is not found", name);
                                return new NotFoundException("Sponsor not found: " + name);
                            }))
                    .collect(Collectors.toCollection(ArrayList::new)); // ✅ MUTABLE

            project.getProjectSponsor().clear();   // ✅ safe
            project.getProjectSponsor().addAll(sponsors);
        }



        // Update type if provided
        if (request.getTypeName() != null && !request.getTypeName().isBlank()) {
            Type type = typeRepository.findByName(request.getTypeName())
                    .orElseThrow(() -> new NotFoundException("Type not found: " + request.getTypeName()));
            project.setType(type);   // assign type to project
        }

        // Save updated project
        project = projectRepository.save(project);

        return modelMapper.map(project, ProjectResponse.class);
    }

}
