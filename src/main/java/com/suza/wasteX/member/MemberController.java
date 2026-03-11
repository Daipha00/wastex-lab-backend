package com.suza.wasteX.member;

import com.suza.wasteX.DTO.MemberRequest;
import com.suza.wasteX.DTO.MemberResponse;
import com.suza.wasteX.DTO.StartupResponse;
import com.suza.wasteX.projectActivity.Activity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/members/")
@Tag(name = "Member API", description = "This is API collection for managing Member")
public class MemberController {
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "create a member",
            description = "This endpoint is for assign a member to their projects activity"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200",description = "Member created successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponse.class)) }),

            @ApiResponse(responseCode = "404",description = "Member not Found", content = @Content)
    })
    @PostMapping("{activityId}")
    ResponseEntity<MemberResponse> createMember(@PathVariable Long activityId, @RequestBody MemberRequest request) {
        MemberResponse response = memberService.createMember(activityId,request);
        return response!= null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    @Operation(
            summary = "get a members by activity",
            description = "This endpoint is for getting members of a particular project activity"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200",description = "Member created successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponse.class)) }),

            @ApiResponse(responseCode = "404",description = "Member not Found", content = @Content)
    })
    @GetMapping("activity/{activityId}")
    ResponseEntity<List<MemberResponse>> getMemberByActivity(@PathVariable Long activityId) {
        List<MemberResponse> response = memberService.getMemberByActivity(activityId);
        return response!= null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "get a list of  members",
            description = "This endpoint is for getting the list of members"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200",description = "Member created successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponse.class)) }),

            @ApiResponse(responseCode = "404",description = "Member not Found", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> memberResponse = memberService.getAllMembers();
        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }
    @Operation(
            summary = "get a member",
            description = "This endpoint is for getting a member"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200",description = "Member created successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponse.class)) }),

            @ApiResponse(responseCode = "404",description = "Member not Found", content = @Content)
    })
    @GetMapping("{id}")
    ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        MemberResponse response = memberService.getMemberById(id);
        return response!= null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }


    @Operation(
            summary = "delete a member",
            description = "This endpoint is for deleting a member"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200",description = "Member created successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponse.class)) }),

            @ApiResponse(responseCode = "404",description = "Member not Found", content = @Content)
    })
    @DeleteMapping("{id}")
    ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        Boolean response = memberService.deleteMemberById(id);
        return response ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("project/{projectId}")
    public List<MemberResponse> getMembersByProject(@PathVariable Long projectId) {
        List<Member> members = memberService.getMembersByProjectId(projectId);
        return members.stream()
                .map(member -> modelMapper.map(member, MemberResponse.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/project/{projectId}")
    public ResponseEntity<MemberResponse> addMemberToProject(
            @PathVariable Long projectId,
            @RequestBody MemberRequest request
    ) {
        MemberResponse response = memberService.addMemberToProject(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{memberId}/status")
    public ResponseEntity<MemberResponse> updateMemberStatus(
            @PathVariable Long memberId,
            @RequestParam String status) {

        MemberResponse response = memberService.updateStatus(memberId, status);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long id,
            @RequestBody MemberRequest request) {

        MemberResponse response = memberService.updateMemberById(id, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("me")
    public List<MemberResponse> getMyParticipations(@RequestParam String email) {

        List<Member> members = memberService.getMembersByEmail(email);

        return members.stream()
                .map(member -> {
                    MemberResponse response = modelMapper.map(member, MemberResponse.class);

                    if (member.getProject() != null) {
                        response.setProjectName(member.getProject().getProjectName());
                    }

                    return response;
                })
                .toList();
    }

}
