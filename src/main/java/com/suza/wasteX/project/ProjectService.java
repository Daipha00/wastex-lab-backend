package com.suza.wasteX.project;

import com.suza.wasteX.DTO.*;
import com.suza.wasteX.DTO.StatusDto.ProjectStatusRequest;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.projectActivity.Activity;
import com.suza.wasteX.projectActivity.ActivityRepository;
import com.suza.wasteX.sponsor.Sponsor;
import com.suza.wasteX.sponsor.SponsorRepository;
import com.suza.wasteX.statuses.Status;
import com.suza.wasteX.statuses.StatusRepository;
import com.suza.wasteX.statuses.activityStatus.ActivityStatus;
import com.suza.wasteX.statuses.projectStatus.ProjectStatus;
import com.suza.wasteX.statuses.projectStatus.ProjectStatusRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

        List<Sponsor> sponsors = Optional.ofNullable(request.getProjectSponsor())
                .orElse(List.of()) // or `Collections.emptyList()`
                .stream()
                .map(id -> sponsorRepository.findById(id)
                        .orElseThrow(() -> {
                            log.info("Sponsor with id {} is not found", id);
                            return new NotFoundException("Sponsor id is not found");
                        }))
                .toList();

        if (request.getStatuses() != null) {
            Project finalProject = project;
            List<ProjectStatus> statuses = request.getStatuses().stream()
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
            project.setStatuses(statuses);
        }

        project.setProjectSponsor(sponsors);
        project = projectRepository.save(project);
        return modelMapper.map(project, ProjectResponse.class);
    }

    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(()-> {
                    log.info("Project with id {} is not found", id);
                    return new NotFoundException("Project id is not found");
                });

        project.setProjectName(request.getProjectName());
        project.setProjectBudget(request.getProjectBudget());
        project.setDescription(request.getDescription());
        project.setActive(request.isActive());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        List<Sponsor> sponsors = request.getProjectSponsor()
                .stream()
                .map(sponsorId -> sponsorRepository.findById(sponsorId).orElseThrow(() -> {
                    log.info("Sponsor with id {} is not found", sponsorId);
                    return new NotFoundException("Sponsor id is not found");
                })).toList();
                project.setProjectSponsor(sponsors);
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
        response.setTypes(response.getTypes().stream().toList());
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
    public ProjectResponse updateProjectSponsors(Long projectId, List<Long> sponsorIds) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> {
            log.info("Project with id {} is not found", projectId);
            return new NoResultException("Project id is not found");
        });

        List<Sponsor> sponsors = sponsorIds.stream()
                .map(id -> sponsorRepository.findById(id).orElseThrow(() -> {
                    log.info("Sponsor with id {} is not found", projectId);
                    return new NotFoundException("Sponsor id is not found");
                } ))
                .collect(Collectors.toList());

        project.setProjectSponsor(sponsors);
        project = projectRepository.save(project);

        return modelMapper.map(project, ProjectResponse.class);
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



}
