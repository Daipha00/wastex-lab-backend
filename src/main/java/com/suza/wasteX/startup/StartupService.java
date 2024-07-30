package com.suza.wasteX.startup;

import com.suza.wasteX.DTO.StartupRequest;
import com.suza.wasteX.DTO.StartupResponse;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.project.Project;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Builder
@RequiredArgsConstructor
@Slf4j
public class StartupService {

    private final StartupRepository startUpRepository;
    private final ModelMapper modelMapper;

    public StartupResponse createStartUp(StartupRequest request) {
        Startup startUp = modelMapper.map(request, Startup.class);
        startUp = startUpRepository.save(startUp);
        return modelMapper.map(startUp, StartupResponse.class);
    }

    public List<StartupResponse> getAllStartUp() {
        List<Startup> startups = startUpRepository.findAll();
        return startups
                .stream()
                .map(startup -> modelMapper.map(startup, StartupResponse.class))
                .collect(Collectors.toList());
    }
    public Optional<StartupResponse> getStartupByName(String startUp) {

        Optional<Startup> startUpOptional = startUpRepository.findByStartup(startUp);
        if(startUpOptional.isPresent()) {
            return Optional.ofNullable(modelMapper.map(startUpOptional, StartupResponse.class));
        }
        else return Optional.empty();
    }

    public StartupResponse updateStartup(Long id, StartupRequest startupRequest) {
            Optional<Startup> startup = startUpRepository.findById(id);
            if(startup.isPresent()) {
                Startup new_startup = startup.get();
                new_startup.setStartup(startupRequest.getStartup());
                new_startup.setWebsite(startupRequest.getWebsite());
                new_startup.setPhoneNumber(startupRequest.getPhoneNumber());
                new_startup.setAddress(startupRequest.getAddress());
                new_startup.setActive(startupRequest.getActive());
                new_startup = startUpRepository.save(new_startup);
                return modelMapper.map(new_startup, StartupResponse.class);
            }
            else {
                log.info("startup with id {} does not exists" ,id);
                return null;
            }
    }

    public boolean deleteStartup(Long id) {
        Optional<Startup> startup = startUpRepository.findById(id);
        if(startup.isPresent()) {
            startUpRepository.delete(startup.get());
            return true;
        }
        else {
            return false;
        }
    }
}
