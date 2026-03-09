package com.suza.wasteX.member;

import com.suza.wasteX.DTO.MemberRequest;
import com.suza.wasteX.DTO.MemberResponse;
import com.suza.wasteX.customException.NotFoundException;
import com.suza.wasteX.project.Project;
import com.suza.wasteX.project.ProjectRepository;
import com.suza.wasteX.projectActivity.Activity;
import com.suza.wasteX.projectActivity.ActivityRepository;
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
public class MemberService {
    private final MemberRepository memberRepository;
    private final ActivityRepository activityRepository;
    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;
    private final EmailService emailService;

    public MemberResponse createMember(Long activityId, MemberRequest request) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity not found"));

        Project project = activity.getProject();

        if (project == null) {
            throw new NotFoundException("Project not found for this activity");
        }

        Member savedMember = null;

        List<Activity> projectActivities = activityRepository.findByProjectId(project.getId());

        for (Activity act : projectActivities) {

            Member member = modelMapper.map(request, Member.class);

            member.setActivity(act);
            member.setProject(project);   // ✅ THIS WAS MISSING

            savedMember = memberRepository.save(member);
        }

        return modelMapper.map(savedMember, MemberResponse.class);
    }

    public List<MemberResponse> getMemberByActivity(Long activityId) {
                activityRepository.findById(activityId)
                .orElseThrow(() -> {
                    log.info("Activity with id {} is not found", activityId);
                    return new NotFoundException("Activity is not found");
                });
        List<Member> members = memberRepository.findAllByActivityId(activityId);
            return members
                    .stream().map(member -> modelMapper.map(member, MemberResponse.class) ).
                    collect(Collectors.toList());
    }
    public List<MemberResponse> getAllMembers() {
      List<Member> members = memberRepository.findAll();
      return members
              .stream().map(member -> modelMapper.map(member, MemberResponse.class) ).
              collect(Collectors.toList());
     }
     public MemberResponse getMemberById(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        return memberOptional
                .map(member ->modelMapper.map(member, MemberResponse.class))
                .orElseThrow(() -> {
                    log.info("Member with id {} is not found ", id);
                    return new NotFoundException("Member not found");
                });
     }
     public MemberResponse updateMember(Long activityId, Long id, MemberRequest request) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(()-> {
                    log.info("Project activity with id {} not found", activityId);
                    return new NotFoundException("Project activity not found");
                });
        Optional<Member> member = memberRepository.findById(id);
        if(member.isEmpty()) {
            log.info("Member with id {} not found ", id);
            throw  new NotFoundException("Member with id not found");
        }
        else {
            Member new_member = member.get();
            new_member.setActivity(activity);
            new_member.setFirstName(request.getFirstName());
            new_member.setMiddleName(request.getMiddleName());
            new_member.setLastName(request.getLastName());
            new_member.setGender(request.getGender());
            new_member.setAge(request.getAge());
            new_member.setEmail(request.getEmail());
            new_member.setCountry(request.getCountry());
            new_member.setInstitution(request.getInstitution());
            new_member = memberRepository.save(new_member);
            return modelMapper.map(new_member, MemberResponse.class);
        }
     }

    public MemberResponse updateMemberById(Long id, MemberRequest request) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.setFirstName(request.getFirstName());
        member.setMiddleName(request.getMiddleName());
        member.setLastName(request.getLastName());
        member.setGender(request.getGender());
        member.setAge(request.getAge());
        member.setEmail(request.getEmail());
        member.setCountry(request.getCountry());
        member.setPhoneNumber(request.getPhoneNumber());
        member.setInstitution(request.getInstitution());

        memberRepository.save(member);

        return modelMapper.map(member, MemberResponse.class);
    }



    public Boolean deleteMemberById(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        if(memberOptional.isPresent()) {
            memberRepository.delete(memberOptional.get());
            return true;
        }
        else {
            log.info("Member with id {} is not found ", id);
            throw new NotFoundException("Member id not found");
        }
    }

//    statistic API
    public Long  countTotalMembers() {
        return memberRepository.countTotalMembers();
    }
    public Long countMaleMembers() {
        return memberRepository.countMaleMembers();
    }
    public Long countFemaleMembers() {
        return memberRepository.countFemaleMembers();
    }
    public Long countMembersByActivity(Long activityId) {
        return memberRepository.countTotalMembersByActivity(activityId);
    }

    public Long countMaleMembersByActivity(Long activityId) {
        return memberRepository.countMaleMembersByActivityId(activityId);
    }
    public Long countFemaleMembersByActivity(Long activityId) {
        return memberRepository.countFemaleMembersByActivityId(activityId);
    }

    public List<Member> getMembersByProjectId(Long projectId) {
        return memberRepository.findByProjectId(projectId);
    }

    public MemberResponse addMemberToProject(Long projectId, MemberRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Member member = Member.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .age(request.getAge())
                .email(request.getEmail())
                .country(request.getCountry())
                .institution(request.getInstitution())
                .phoneNumber(request.getPhoneNumber())
                .status(Member.MemberStatus.PENDING)
                .project(project)
                .build();

        Member savedMember = memberRepository.save(member);

        return mapToResponse(savedMember);
    }

    // inside MemberService
    private MemberResponse mapToResponse(Member member) {
        if (member == null) return null;

        return MemberResponse.builder()
                .id(member.getId())
                .firstName(member.getFirstName())
                .middleName(member.getMiddleName())
                .lastName(member.getLastName())
                .gender(member.getGender())
                .age(member.getAge())
                .email(member.getEmail())
                .country(member.getCountry())
                .institution(member.getInstitution())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }

    public MemberResponse updateStatus(Long memberId, String status) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.setStatus(Member.MemberStatus.valueOf(status.toUpperCase()));

        Member saved = memberRepository.save(member);

        try {
            emailService.sendStatusEmail(saved);
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }

        return modelMapper.map(saved, MemberResponse.class);
    }

    public List<Member> getMembersByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
