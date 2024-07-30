package com.suza.wasteX.skill;

import com.suza.wasteX.DTO.SkillRequest;
import com.suza.wasteX.DTO.SkillResponse;
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
public class SkillService {
    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;

    public SkillResponse addSkill(SkillRequest request) {
        Skill skill = modelMapper.map(request, Skill.class);
        skill = skillRepository.save(skill);
        return modelMapper.map(skill, SkillResponse.class);

    }
    public List<SkillResponse> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        return skills
                .stream().map(skill -> modelMapper.map(skill, SkillResponse.class))
                .collect(Collectors.toList());
    }


    public SkillResponse getSkillById(Long skillId) {
        Optional<Skill> skillOptional = skillRepository.findById(skillId);
        return skillOptional
                .map(skill -> modelMapper.map(skill, SkillResponse.class))
                    .orElseThrow(()-> {
                        log.info("Skill with id {} is not found ", skillId);
                        return new NotFoundException("Skill id is not found");
                    });
    }


    public SkillResponse updateSkill(Long skillId, SkillRequest request) {
        Optional<Skill> skillOptional = skillRepository.findById(skillId);
        if(skillOptional.isPresent()) {
            Skill new_skill = skillOptional.get();
            new_skill.setTitle(request.getTitle());
            skillRepository.save(new_skill);
        }
        return skillOptional
                .map(skill -> modelMapper.map(skill, SkillResponse.class))
                .orElseThrow(()-> {
                    log.info("Skill with id {} is not found ", skillId);
                    return new NotFoundException("Skill id is not found");
                });
    }

    public Boolean deleteSkill(Long skillId) {
        Optional<Skill> skillOptional = skillRepository.findById(skillId);
        if(skillOptional.isPresent()) {
            skillRepository.delete(skillOptional.get());
            return true;
        }
        else {
            log.info("Skill with id {} is not found", skillId);
            throw new NotFoundException("Skill id is not found");
        }
    }
}
