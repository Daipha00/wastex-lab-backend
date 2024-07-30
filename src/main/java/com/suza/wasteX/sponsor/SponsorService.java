package com.suza.wasteX.sponsor;

import com.suza.wasteX.DTO.SponsorRequest;
import com.suza.wasteX.DTO.SponsorResponse;
import com.suza.wasteX.customException.NotFoundException;
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
public class SponsorService {
    private final SponsorRepository sponsorRepository;
    private final ModelMapper modelMapper;

    public SponsorResponse addSponsor(SponsorRequest request) {
        Sponsor sponsor = modelMapper.map(request, Sponsor.class);
        sponsor = sponsorRepository.save(sponsor);
        return modelMapper.map(sponsor, SponsorResponse.class);

    }
    public List<SponsorResponse> getAllSponsors() {
        List<Sponsor> sponsors = sponsorRepository.findAll();
        return sponsors
                .stream().map(sponsor -> modelMapper.map(sponsor, SponsorResponse.class))
                .collect(Collectors.toList());
    }


    public SponsorResponse getSponsorById(Long sponsorId) {
        Optional<Sponsor> sponsorOptional = sponsorRepository.findById(sponsorId);
        return sponsorOptional
                .map(sponsor -> modelMapper.map(sponsor, SponsorResponse.class))
                .orElseThrow(()-> {
                    log.info("Sponsor with id {} is not found ", sponsorId);
                    return new NotFoundException("Sponsor id is not found");
                });
    }


    public SponsorResponse updateSponsor(Long sponsorId, SponsorRequest request) {
        Optional<Sponsor> sponsorOptional = sponsorRepository.findById(sponsorId);
        if(sponsorOptional.isPresent()) {
            Sponsor new_sponsor = sponsorOptional.get();
            new_sponsor.setName(request.getName());
            sponsorRepository.save(new_sponsor);
        }
        return sponsorOptional
                .map(sponsor -> modelMapper.map(sponsor, SponsorResponse.class))
                .orElseThrow(()-> {
                    log.info("Sponsor with id {} is not found ", sponsorId);
                    return new NotFoundException("Sponsor id is not found");
                });
    }

    public Boolean deleteSponsor(Long sponsorId) {
        Optional<Sponsor> sponsorOptional = sponsorRepository.findById(sponsorId);
        if(sponsorOptional.isPresent()) {
            sponsorRepository.delete(sponsorOptional.get());
            return true;
        }
        else {
            log.info("Sponsor with id {} is not found", sponsorId);
            throw new NotFoundException("Sponsor id is not found");
        }
    }
}
