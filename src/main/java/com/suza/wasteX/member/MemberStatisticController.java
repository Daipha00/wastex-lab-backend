package com.suza.wasteX.member;

import com.suza.wasteX.DTO.MembersCountResponse;
import com.suza.wasteX.DTO.ProjectCountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/member/counts/")
@RequiredArgsConstructor
@Tag(name = "Statistics API")
public class MemberStatisticController {
    private final MemberService memberService;

    @Operation(
            summary = "get total number of members",
            description = "This endpoint is for getting total number of members , male member, female member"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200", description = "Member count successfully",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectCountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Member count not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<?> countMembers() {
        Long totalMembers = memberService.countTotalMembers();
        Long maleMembers = memberService.countMaleMembers();
        Long femaleMembers = memberService.countFemaleMembers();
        return ResponseEntity.status(HttpStatus.OK).body(new MembersCountResponse(totalMembers, maleMembers, femaleMembers));
    }

    @Operation(
            summary = "get total number of members in activities",
            description = "This endpoint is for getting total number of members per activities , male members and female members"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200", description = "Member count successfully",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectCountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Member count not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("{activityId}")
    public ResponseEntity<?> countMembersByActivity(@PathVariable Long activityId) {
        Long totalActivityMembers = memberService.countMembersByActivity(activityId);
        Long maleActivityMembers = memberService.countMaleMembersByActivity(activityId);
        Long femaleActivityMembers = memberService.countFemaleMembersByActivity(activityId);
        return ResponseEntity.status(HttpStatus.OK).body(new MembersCountResponse(totalActivityMembers, maleActivityMembers, femaleActivityMembers));
    }
}
