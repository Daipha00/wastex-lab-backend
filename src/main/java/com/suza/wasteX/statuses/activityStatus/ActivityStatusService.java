package com.suza.wasteX.statuses.activityStatus;

import com.suza.wasteX.DTO.*;
import com.suza.wasteX.DTO.StatusDto.ActivityStatusRequest;
import com.suza.wasteX.DTO.StatusDto.ActivityStatusResponse;
import com.suza.wasteX.customException.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityStatusService {
    private final ActivityStatusRepository repository;
    private final ModelMapper modelMapper;

    public ActivityStatusResponse addActivityStatus(ActivityStatusRequest request) {
        ActivityStatus activityStatus = modelMapper.map(request, ActivityStatus.class);
        repository.save(activityStatus);
        return modelMapper.map(activityStatus, ActivityStatusResponse.class);
    }

    public List<ActivityStatusResponse> getAllActivityStatuses() {
        List<ActivityStatus> activityStatuses = repository.findAll();
        return activityStatuses
                .stream()
                .map(activityStatus ->
                        modelMapper.map(activityStatus, ActivityStatusResponse.class))
                .collect(Collectors.toList());
    }

    public ActivityStatusResponse getActivityStatusById(Long statusId) {
        Optional<ActivityStatus> activityStatus = repository.findById(statusId);
        return activityStatus
                .map( status -> modelMapper.map(status, ActivityStatusResponse.class))
                .orElse(null);
    }

    @Transactional
    public ActivityResponse updateActivityStatus(Long projectId, Long statusId, ActivityStatusRequest statusRequest) {
        Optional<ActivityStatus> optionalProject = repository.findById(projectId);
        Optional<ActivityStatus> optionalActivityStatus  = repository.findById(statusId);

        if(optionalProject.isPresent()) {
            ActivityStatus activity = optionalProject.get();
            if(optionalActivityStatus.isPresent()){
                ActivityStatus status = optionalActivityStatus.get();
                repository.save(status);
            }
            return modelMapper.map(activity, ActivityResponse.class);
        }

        log.info("Project with id {} is not found ", projectId);
        throw new NotFoundException("Project with id not found");
    }

    public Boolean deleteActivityStatus(Long statusId) {
        Optional<ActivityStatus> status = repository.findById(statusId);
        if(status.isPresent()) {
            repository.delete(status.get());
            return true;
        }
        else {
            return false;
        }

    }
}
