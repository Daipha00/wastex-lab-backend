package com.suza.wasteX.skill;

import com.suza.wasteX.DTO.SkillRequest;
import com.suza.wasteX.DTO.SkillResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/skills/")
@RequiredArgsConstructor
@Tag(name = "Skill API", description = "This is API Collection for Manage Skill")
public class SkillController {
    private final SkillService skillService;


    @Operation(
            summary = "create a skill",
            description = "This endpoint is for creating skill"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Skill created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SkillRepository.class))}),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)
    })
    @PostMapping
    ResponseEntity<SkillResponse> createSkill(@RequestBody SkillRequest request) {
        SkillResponse response = skillService.addSkill(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(
            summary = "get list of skill",
            description = "This endpoint is for getting a list of skill"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Skill created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SkillRepository.class))}),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @GetMapping
    ResponseEntity<List<SkillResponse>> getAllSkill() {
        List<SkillResponse> response = skillService.getAllSkills();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "get a skill",
            description = "This endpoint is for a getting skill"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Skill created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SkillRepository.class))}),
            @ApiResponse(responseCode = "404", description = "skill not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @GetMapping("{skillId}")
    ResponseEntity<SkillResponse> getSkillById(@PathVariable Long skillId) {
        SkillResponse response = skillService.getSkillById(skillId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "update a skill",
            description = "This endpoint is for update skill"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Skill created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SkillRepository.class))}),
            @ApiResponse(responseCode = "404", description = "skill not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @PutMapping("{skillId}")
    ResponseEntity<SkillResponse> updateSkill(@PathVariable Long skillId, @RequestBody SkillRequest request) {
        SkillResponse response = skillService.updateSkill(skillId, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();

    }
    @Operation(
            summary = "delete a skill",
            description = "This endpoint is for deleting skill"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Skill created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SkillRepository.class))}),
            @ApiResponse(responseCode = "404", description = "skill not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @DeleteMapping("{skillId}")
    ResponseEntity<Void> deleteSkill(@PathVariable Long skillId) {
        Boolean response = skillService.deleteSkill(skillId);
        return response ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();


    }


}
