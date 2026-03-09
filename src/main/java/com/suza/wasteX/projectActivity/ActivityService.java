package com.suza.wasteX.projectActivity;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.suza.wasteX.DTO.*;
import com.suza.wasteX.DTO.StatusDto.ActivityStatusRequest;
import com.suza.wasteX.DTO.StatusDto.ActivityStatusResponse;
import com.suza.wasteX.customException.NotFoundException;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
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


    private ActivityResponse mapToResponse(Activity activity) {

        List<SponsorResponse> sponsorResponses =
                activity.getActivitySponsor() == null
                        ? List.of()
                        : activity.getActivitySponsor()
                        .stream()
                        .map(sponsor -> SponsorResponse.builder()
                                .id(sponsor.getId())
                                .name(sponsor.getName())
                                .build())
                        .toList();

        return ActivityResponse.builder()
                .id(activity.getId())
                .projectActivityName(activity.getProjectActivityName())
                .description(activity.getDescription())
                .duration(activity.getDuration())
                .unit(activity.getUnit())
                .startDate(activity.getStartDate())
                .endDate(activity.getEndDate())
                .activitySponsor(sponsorResponses)
                .build();
    }


    @Transactional
    public ActivityResponse createActivity(Long projectId, ActivityRequest request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project with id " + projectId + " not found"));

        Activity activity = new Activity();
        activity.setProject(project);
        activity.setProjectActivityName(request.getProjectActivityName());
        activity.setDescription(request.getDescription());
        activity.setDuration(request.getDuration());
        activity.setUnit(request.getUnit());
        activity.setStartDate(request.getStartDate());
        activity.setEndDate(request.getEndDate());

        // 🔥 FETCH SPONSORS FROM DATABASE
        if (request.getActivitySponsor() != null && !request.getActivitySponsor().isEmpty()) {

            List<Sponsor> sponsors = request.getActivitySponsor().stream()
                    .map(id -> sponsorRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Sponsor not found with id " + id)))
                    .toList();

            activity.setActivitySponsor(sponsors);
        } else {
            activity.setActivitySponsor(List.of());
        }

        Activity savedActivity = activityRepository.save(activity);

        return mapToResponse(savedActivity);
    }


    public List<ActivityResponse> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activities.stream()
                .map(activity -> modelMapper.map(activity, ActivityResponse.class))
                .collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(Long activityId) {
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        return optionalActivity.map(activity -> modelMapper.map(activity, ActivityResponse.class))
                .orElseThrow(() -> {
                    log.info("Activity with id {} is not found", activityId);
                    return new NotFoundException("Activity id not found");
                });
    }

    @Transactional
    public ActivityResponse updateActivity(Long projectId, Long activityId, ActivityRequest request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project with id " + projectId + " not found"));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity not found"));

        activity.setProject(project);
        activity.setProjectActivityName(request.getProjectActivityName());
        activity.setDescription(request.getDescription());
        activity.setDuration(request.getDuration());
        activity.setUnit(request.getUnit());
        activity.setStartDate(request.getStartDate());
        activity.setEndDate(request.getEndDate());

        // 🔥 UPDATE SPONSORS
        if (request.getActivitySponsor() != null) {

            List<Sponsor> sponsors = request.getActivitySponsor().stream()
                    .map(id -> sponsorRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Sponsor not found with id " + id)))
                    .collect(Collectors.toList()); // NOT .toList()

            activity.setActivitySponsor(new ArrayList<>(sponsors));

        }

        Activity savedActivity = activityRepository.save(activity);

        return mapToResponse(savedActivity);
    }


    public List<ActivityResponse> getActivitiesByProjectId(Long projectId) {

        List<Activity> activities = activityRepository.findByProjectId(projectId);

        return activities.stream()
                .map(activity -> {

                    List<SponsorResponse> sponsorResponses =
                            activity.getActivitySponsor() == null
                                    ? Collections.emptyList()
                                    : activity.getActivitySponsor()
                                    .stream()
                                    .map(sponsor -> SponsorResponse.builder()
                                            .id(sponsor.getId())
                                            .name(sponsor.getName())
                                            .build())
                                    .collect(Collectors.toList());

                    return ActivityResponse.builder()
                            .id(activity.getId())
                            .projectActivityName(activity.getProjectActivityName())
                            .description(activity.getDescription())
                            .duration(activity.getDuration())
                            .unit(activity.getUnit())
                            .startDate(activity.getStartDate())
                            .endDate(activity.getEndDate())
                            .activitySponsor(sponsorResponses) // Always non-null
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
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

    @Transactional
    public List<MemberResponse> uploadMembers(Long activityId, MultipartFile file) throws IOException {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity not found"));

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<MemberResponse> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(MemberResponse.class);

            CsvToBean<MemberResponse> csvToBean = new CsvToBeanBuilder<MemberResponse>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<MemberResponse> csvMembers = csvToBean.parse();

            List<Member> members = csvMembers.stream()
                    .map(dto -> {
                        Member member = new Member();
                        member.setFirstName(dto.getFirstName());
                        member.setMiddleName(dto.getMiddleName());
                        member.setLastName(dto.getLastName());
                        member.setGender(dto.getGender());
                        member.setAge(dto.getAge());
                        member.setEmail(dto.getEmail());
                        member.setCountry(dto.getCountry());
                        member.setInstitution(dto.getInstitution());
                        member.setPhoneNumber(dto.getPhoneNumber());
                        member.setActivity(activity);
                        return member;
                    })
                    .collect(Collectors.toList());

            memberRepository.saveAll(members);
            return members.stream()
                    .map(member -> modelMapper.map(member, MemberResponse.class))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public ActivityResponse addSponsorsToActivity(Long activityId, List<Long> sponsorIds) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        List<Sponsor> currentSponsors = activity.getActivitySponsor();
        List<Sponsor> newSponsors = sponsorIds.stream()
                .map(id -> sponsorRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Sponsor not found")))
                .filter(newSponsor -> !currentSponsors.contains(newSponsor))
                .toList();

        currentSponsors.addAll(newSponsors);
        activity.setActivitySponsor(currentSponsors);

        activity = activityRepository.save(activity);
        return mapToResponse(activity);

    }

    @Transactional
    public ActivityResponse updateActivitySponsors(Long activityId, List<Long> sponsorIds) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> {
                    log.info("Project with id {} is not found", activityId);
                    return new NoResultException("Project id is not found");
                });

        List<Sponsor> sponsors = sponsorIds.stream()
                .map(id -> sponsorRepository.findById(id)
                        .orElseThrow(() -> {
                            log.info("Sponsor with id {} is not found", activityId);
                            return new NotFoundException("Sponsor id is not found");
                        }))
                .collect(Collectors.toList());

        activity.setActivitySponsor(sponsors);
        activity = activityRepository.save(activity);

        return mapToResponse(activity);
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
        newStatus.setStatusDate(statusRequest.getStatusDate()); // Date
        newStatus.setActivity(activity);

        activity.getStatuses().add(newStatus);
        activity = activityRepository.save(activity);

        return modelMapper.map(activity, ActivityResponse.class);
    }

    // statistic API for status
    public Long countActivityStatusByActivity(Long activityId) {
        return activityStatusRepository.countActivityStatusByActivity(activityId);
    }
}
