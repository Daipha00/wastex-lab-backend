package com.suza.wasteX.projectType;


import com.suza.wasteX.DTO.ActivityRequest;
import com.suza.wasteX.DTO.ActivityResponse;
import com.suza.wasteX.DTO.TypeRequest;
import com.suza.wasteX.DTO.TypeResponse;
import com.suza.wasteX.customException.NotFoundException;

import com.suza.wasteX.project.Project;
import com.suza.wasteX.project.ProjectRepository;
import com.suza.wasteX.projectActivity.Activity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TypeService {
    private final TypeRepository repository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    public TypeResponse createType(TypeRequest request) {
        Type type = modelMapper.map(request, Type.class);
        type = repository.save(type);
        return modelMapper.map(type, TypeResponse.class);
    }
    public TypeResponse addProjectType(Long projectId, TypeRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.info("Project with id  {}  not found",projectId);
                    return new NotFoundException("Project id not found");
                });
        Type type = modelMapper.map(request, Type.class);
        project.setType(type);
        type = repository.save(type);
        return modelMapper.map(type, TypeResponse.class);
    }


    public List<TypeResponse> getAllTypes() {
        List<Type> types = repository.findAll();
        return types
                .stream()
                .map(type -> modelMapper.map(type, TypeResponse.class))
                .collect(Collectors.toList());
    }

    public TypeResponse getTypeById(Long typeId) {
        Optional<Type> optionalType = repository.findById(typeId);
        return optionalType
                .map(type -> modelMapper.map(type, TypeResponse.class))
                .orElseThrow(()-> {
                    log.info("Type with id {} is not found ", typeId);
                    return new NotFoundException("Type id not found");
                });
    }

    public Map<String, Long> getProjectTypeCounts() {
        Map<String, Long> counts = new HashMap<>();

        Long hackathonCount = repository.countByNameIgnoreCase("hackathon");
        Long incubationCount = repository.countByNameIgnoreCase("incubation");

        counts.put("hackathon", hackathonCount);
        counts.put("incubation", incubationCount);

        return counts;
    }

    public Long getTotalTypesCount() {
        return repository.count();  // counts all rows in project_type table
    }



    public TypeResponse updateType(Long typeId, TypeRequest request) {
        Optional<Type> optionalType = repository.findById(typeId);
        if(optionalType.isPresent()) {
            Type new_type = optionalType.get();
            new_type.setName(request.getName());
            repository.save(new_type);
        }
        return optionalType
                .map(type -> modelMapper.map(type, TypeResponse.class))
                .orElseThrow(() -> {
                    log.info("Type with id {} is not found", typeId);
                    return new NotFoundException("Type id not found");
                });
    }

    public Boolean deleteType(Long typeId) {
        Optional<Type> optionalType = repository.findById(typeId);
        if(optionalType.isPresent()) {
            repository.delete(optionalType.get());
            return true;
        }
        else {
            log.info("Type with id {} is not found", typeId);
            throw new NotFoundException("Type id not found");
        }
    }
}
