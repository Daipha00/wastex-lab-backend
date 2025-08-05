package com.suza.wasteX.statuses.projectStatus;

import com.suza.wasteX.DTO.*;
import com.suza.wasteX.DTO.StatusDto.ProjectStatusRequest;
import com.suza.wasteX.DTO.StatusDto.ProjectStatusResponse;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.project.Project;
import com.suza.wasteX.project.ProjectRepository;
import com.suza.wasteX.statuses.Status;
import com.suza.wasteX.statuses.StatusRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectStatusService {
    private final ProjectStatusRepository repository;
    private final ProjectRepository projectRepository;
    private final StatusRepository statusRepository;
    private final ModelMapper modelMapper;

    public ProjectStatusResponse addProjectStatus(ProjectStatusRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new NotFoundException("Project with id " + request.getProjectId() + " not found"));

        Status status = statusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new NotFoundException("Status with id " + request.getStatusId() + " not found"));

        ProjectStatus projectStatus = new ProjectStatus();
        projectStatus.setProject(project);
        projectStatus.setStatus(status);
        projectStatus.setStatusDate(request.getStatusDate());

        projectStatus = repository.save(projectStatus);

        return modelMapper.map(projectStatus, ProjectStatusResponse.class);
    }
    @Transactional
    public ProjectResponse updateProjectStatus(Long projectId, Long statusId, ProjectStatusRequest statusRequest) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        Optional<ProjectStatus> optionalProjectStatus  = repository.findById(statusId);

        if(optionalProject.isPresent()) {
            Project project = optionalProject.get();
            if(optionalProjectStatus.isPresent()){
                ProjectStatus status = optionalProjectStatus.get();
                repository.save(status);
            }
            return modelMapper.map(project, ProjectResponse.class);
        }

        log.info("Project with id {} is not found ", projectId);
        throw new NotFoundException("Project with id not found");
    }

    public List<ProjectStatusResponse> getAllProjectStatuses() {
        List<ProjectStatus> projectStatuses = repository.findAll();
        return projectStatuses
                .stream()
                .map(projectStatus -> modelMapper.map(projectStatus, ProjectStatusResponse.class))
                .collect(Collectors.toList());
    }

    public ProjectStatusResponse getProjectStatus(Long statusId) {
        Optional<ProjectStatus> projectStatus = repository.findById(statusId);
         return projectStatus
                 .map( status -> modelMapper.map(status, ProjectStatusResponse.class))
                 .orElse(null);

    }

    public boolean deleteProjectStatus(Long statusId) {
        Optional<ProjectStatus> status = repository.findById(statusId);
        if(status.isPresent()) {
            repository.delete(status.get());
            return true;
        }
        else {
            return false;
        }
    }
}

