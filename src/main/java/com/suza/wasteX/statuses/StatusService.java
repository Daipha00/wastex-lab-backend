package com.suza.wasteX.statuses;


import com.suza.wasteX.DTO.StatusDto.StatusRequest;
import com.suza.wasteX.DTO.StatusDto.StatusResponse;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.project.Project;
import com.suza.wasteX.project.ProjectRepository;
import com.suza.wasteX.statuses.projectStatus.ProjectStatus;
import com.suza.wasteX.statuses.projectStatus.ProjectStatusRepository;
import com.suza.wasteX.statuses.projectStatus.StatusName;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusService {
    private final StatusRepository statusRepository;
    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;
    private final ProjectStatusRepository projectStatusRepository;

    public StatusResponse addStatus(StatusRequest request) {
        Status status = modelMapper.map(request, Status.class);
        status = statusRepository.save(status);
        return modelMapper.map(status, StatusResponse.class);

    }
    public List<StatusResponse> getAllStatuses() {
        List<Status> statuses = statusRepository.findAll();
        return statuses
                .stream().map(status -> modelMapper.map(status, StatusResponse.class))
                .collect(Collectors.toList());
    }


    public StatusResponse getStatusById(Long statusId) {
        Optional<Status> statusOptional = statusRepository.findById(statusId);
        return statusOptional
                .map(status -> modelMapper.map(status, StatusResponse.class))
                .orElseThrow(()-> {
                    log.info("Status with id {} is not found ", statusId);
                    return new NotFoundException("Status id is not found");
                });
    }


//    public StatusResponse updateStatus(Long statusId, StatusRequest request) {
//        Optional<Status> statusOptional = statusRepository.findById(statusId);
//        if(statusOptional.isPresent()) {
//            Status new_status = statusOptional.get();
//            new_status.setName(request.getName());
//            statusRepository.save(new_status);
//        }
//        return statusOptional
//                .map(status -> modelMapper.map(status, StatusResponse.class))
//                .orElseThrow(()-> {
//                    log.info("Status with id {} is not found ", statusId);
//                    return new NotFoundException("Status id is not found");
//                });
//    }

    public Boolean deleteStatus(Long statusId) {
        Optional<Status> statusOptional = statusRepository.findById(statusId);
        if(statusOptional.isPresent()) {
            statusRepository.delete(statusOptional.get());
            return true;
        }
        else {
            log.info("Status with id {} is not found", statusId);
            throw new NotFoundException("Status id is not found");
        }
    }

    public ProjectStatus addOrUpdateProjectStatus(Project project, StatusName newStatusName, Date statusDate) {
        Status statusEntity = statusRepository.findByName(newStatusName)
                .orElseThrow(() -> new RuntimeException("Status not configured: " + newStatusName));

        // Find latest ProjectStatus for project (by statusDate or audit createdAt)
        Optional<ProjectStatus> latestOpt = projectStatusRepository.findTopByProjectOrderByStatusDateDesc(project);

        if (latestOpt.isPresent() && latestOpt.get().getStatus().getName() == newStatusName) {
            // nothing to do (status already current)
            return latestOpt.get();
        }
        ProjectStatus ps = new ProjectStatus();
        ps.setProject(project);
        ps.setStatus(statusEntity);
        ps.setStatusDate(statusDate);
        // set any audit fields via Auditable base
        return projectStatusRepository.save(ps);
    }

    @Scheduled(cron = "0 5 0 * * *") // every day at 00:05
    @Transactional
    public void reconcileProjectStatusesDaily() {
        Date today = new Date(); // server timezone — ensure server TZ is correct
        List<Project> all = projectRepository.findAll();

        for (Project p : all) {
            Date start = p.getStartDate(); // assume LocalDate in entity
            Date end = p.getEndDate();

            StatusName desired;
            if (end != null && end.before(today)) {
                desired = StatusName.COMPLETED;
            } else if (start != null && ( !start.after(today) ) && (end == null || !end.before(today))) {
                // start <= today <= end (or end null means ongoing)
                desired = StatusName.ACTIVE;
            } else {
                // start > today or dates not set -> pending
                desired = StatusName.PENDING;
            }
            addOrUpdateProjectStatus(p, desired, today);
        }
    }
}
