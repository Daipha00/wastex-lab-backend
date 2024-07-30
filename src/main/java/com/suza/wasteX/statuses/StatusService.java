package com.suza.wasteX.statuses;


import com.suza.wasteX.DTO.StatusDto.StatusRequest;
import com.suza.wasteX.DTO.StatusDto.StatusResponse;
import com.suza.wasteX.customException.NotFoundException;
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
public class StatusService {
    private final StatusRepository statusRepository;
    private final ModelMapper modelMapper;

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


    public StatusResponse updateStatus(Long statusId, StatusRequest request) {
        Optional<Status> statusOptional = statusRepository.findById(statusId);
        if(statusOptional.isPresent()) {
            Status new_status = statusOptional.get();
            new_status.setName(request.getName());
            statusRepository.save(new_status);
        }
        return statusOptional
                .map(status -> modelMapper.map(status, StatusResponse.class))
                .orElseThrow(()-> {
                    log.info("Status with id {} is not found ", statusId);
                    return new NotFoundException("Status id is not found");
                });
    }

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
}
