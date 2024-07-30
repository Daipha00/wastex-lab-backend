package com.suza.wasteX.projectActivity;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.suza.wasteX.DTO.ActivityRequest;
import com.suza.wasteX.DTO.ActivityResponse;
import com.suza.wasteX.DTO.MemberResponse;
import com.suza.wasteX.DTO.ProjectResponse;
import com.suza.wasteX.DTO.StatusDto.ActivityStatusRequest;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.member.Gender;
import com.suza.wasteX.member.Member;
import com.suza.wasteX.member.MemberRepository;
import com.suza.wasteX.project.Project;
import com.suza.wasteX.project.ProjectRepository;
import com.suza.wasteX.sponsor.Sponsor;
import com.suza.wasteX.sponsor.SponsorRepository;
import com.suza.wasteX.statuses.Status;
import com.suza.wasteX.statuses.StatusRepository;
import com.suza.wasteX.statuses.activityStatus.ActivityStatus;
import com.suza.wasteX.statuses.activityStatus.ActivityStatusRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ProjectRepository projectRepository;
    private final SponsorRepository sponsorRepository;
    private final MemberRepository memberRepository;
    private final StatusRepository statusRepository;
    private final ActivityStatusRepository activityStatusRepository;
    private final ModelMapper modelMapper;

    public List<ActivityResponse> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activities.stream().map(activity -> modelMapper.map(activity, ActivityResponse.class)).collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(Long activityId) {
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        return optionalActivity.map(activity -> modelMapper.map(activity, ActivityResponse.class)).orElseThrow(() -> {
            log.info("Activity with id {} is not found ", activityId);
            return new NotFoundException("Activity id not found");
        });
    }

    public ActivityResponse updateActivity(Long projectId, Long activityId, ActivityRequest request) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project with id " + projectId + " not found"));
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        if (optionalActivity.isPresent()) {
            Activity new_activity = optionalActivity.get();
            new_activity.setProject(project);
            new_activity.setProjectActivityName(request.getProjectActivityName());
            new_activity.setDescription(request.getDescription());
            new_activity.setDuration(request.getDuration());
            new_activity.setUnit(request.getUnit());
            new_activity.setStartDate(request.getStartDate());
            new_activity.setEndDate(request.getEndDate());
            activityRepository.save(new_activity);
        }
        return optionalActivity.map(activity -> modelMapper.map(activity, ActivityResponse.class)).orElseThrow(() -> {
            log.info("Activity with id {} is not found", activityId);
            return new NotFoundException("Activity id not found");
        });

    }


    public Boolean deleteActivity(Long activityId) {
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        if (optionalActivity.isPresent()) {
            activityRepository.delete(optionalActivity.get());
            return true;
        } else {
            log.info("Activity with id {} is not found", activityId);
            throw new NotFoundException("Activity id not found");
        }
    }

    public List<MemberResponse> uploadMembers(Long activityId, MultipartFile file) throws IOException, CsvException {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> {
            log.info("Activity with id {} is not found", activityId);
            return new NotFoundException("Activity not found");
        });

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> records = csvReader.readAll();
            List<Member> members = records.stream().skip(1) // Skip header row
                    .map(record -> {
                        Member member = new Member();
                        member.setEmail(record[1]);
                        member.setFirstName(record[2]);
                        member.setMiddleName(record[3]);
                        member.setLastName(record[4]);
                        member.setAge(Integer.parseInt(record[5]));
                        member.setGender(Gender.valueOf(record[6]));
                        member.setCountry(record[7]);
                        member.setInstitution(record[8]);
                        member.setPhoneNumber(record[9]);
                        member.setActivity(activity);
                        return member;
                    }).collect(Collectors.toList());

            memberRepository.saveAll(members);
            return members.stream().map(member -> modelMapper.map(member, MemberResponse.class)).collect(Collectors.toList());
        }
    }

    @Transactional
    public ActivityResponse addSponsorsToActivity(Long activityId, List<Long> sponsorIds) {


            Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("Activity not found"));

            List<Sponsor> currentSponsors = activity.getActivitySponsor();
            List<Sponsor> newSponsors = sponsorIds.stream()
                    .map(id -> sponsorRepository.findById(id).orElseThrow(() -> new RuntimeException("Sponsor not found")))
                    .filter(newSponsor -> !currentSponsors.contains(newSponsor))
                    .toList();
            currentSponsors.addAll(newSponsors);
            activity.setActivitySponsor(currentSponsors);
            activity = activityRepository.save(activity);
            return modelMapper.map(activity, ActivityResponse.class);
    }
    @Transactional
    public ActivityResponse updateActivitySponsors(Long activityId, List<Long> sponsorIds) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> {
            log.info("Project with id {} is not found", activityId);
            return new NoResultException("Project id is not found");
        });

        List<Sponsor> sponsors = sponsorIds.stream()
                .map(id -> sponsorRepository.findById(id).orElseThrow(() -> {
                    log.info("Sponsor with id {} is not found", activityId);
                    return new NotFoundException("Sponsor id is not found");
                } ))
                .collect(Collectors.toList());

        activity.setActivitySponsor(sponsors);
        activity = activityRepository.save(activity);

        return modelMapper.map(activity, ActivityResponse.class);
    }
    @Transactional
    public ActivityResponse addActivityStatus(Long activityId, ActivityStatusRequest statusRequest) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> {
                    log.info("Activity with id {} is not found", activityId);
                    return new IllegalArgumentException("Invalid activity ID");
                });

        ActivityStatus newStatus = new ActivityStatus();
        Status statusEntity = statusRepository.findById(statusRequest.getId())
                .orElseThrow(() -> {
                    log.info("Activity status with id {} is not found", activityId);
                    return new IllegalArgumentException("Invalid activity status ID");
                });
        newStatus.setStatus(statusEntity);
        newStatus.setStatusDate(statusRequest.getStatusDate());
        newStatus.setActivity(activity);
        activity.getStatuses().add(newStatus);

        activity = activityRepository.save(activity);
        return modelMapper.map(activity, ActivityResponse.class);
    }

//    statistic API for status
    public Long countActivityStatusByActivity(Long activityId) {
        return activityStatusRepository.countActivityStatusByActivity(activityId);
    }
}

