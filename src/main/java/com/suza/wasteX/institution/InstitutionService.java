package com.suza.wasteX.institution;

import com.suza.wasteX.DTO.InstitutionRequest;
import com.suza.wasteX.DTO.InstitutionResponse;
import com.suza.wasteX.customException.NotFoundException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class InstitutionService {
    private final InstitutionRepository repository;
    private final ModelMapper modelMapper;

    public InstitutionResponse createInstitution(InstitutionRequest request) {
        Institution institution = modelMapper.map(request, Institution.class);
        institution = repository.save(institution);
        return modelMapper.map(institution, InstitutionResponse.class);
    }


    public List<InstitutionResponse> getAllInstitutions() {
        List<Institution> institutions = repository.findAll();
        return institutions
                .stream()
                .map(institution -> modelMapper.map(institution, InstitutionResponse.class))
                .collect(Collectors.toList());
    }

    public InstitutionResponse getInstitutionById(Long institutionId) {
        Optional<Institution> optionalInstitution = repository.findById(institutionId);
        return optionalInstitution
                .map(institution -> modelMapper.map(institution, InstitutionResponse.class))
                .orElseThrow(()-> {
                    log.info("Institution with id {} is not found ", institutionId);
                    return new NotFoundException("Institution id not found");
                });
    }

    public InstitutionResponse updateInstitution(Long institutionId, InstitutionRequest request) {
        Optional<Institution> optionalInstitution = repository.findById(institutionId);
        if(optionalInstitution.isPresent()) {
            Institution new_institution = optionalInstitution.get();
            new_institution.setName(request.getName());
            new_institution.setLocation(request.getLocation());
            repository.save(new_institution);
        }
        return optionalInstitution
                .map(institution -> modelMapper.map(institution, InstitutionResponse.class))
                .orElseThrow(() -> {
                    log.info("Institution with id {} is not found", institutionId);
                    return new NotFoundException("Institution id not found");
                });
    }

    public Boolean deleteInstitution(Long institutionId) {
        Optional<Institution> optionalInstitution = repository.findById(institutionId);
        if(optionalInstitution.isPresent()) {
            repository.delete(optionalInstitution.get());
            return true;
        }
        else {
            log.info("Institution with id {} is not found", institutionId);
            throw new NotFoundException("Institution id not found");
        }
    }
}
