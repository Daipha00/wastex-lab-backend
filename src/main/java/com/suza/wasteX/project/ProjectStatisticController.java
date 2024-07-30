package com.suza.wasteX.project;

import com.suza.wasteX.DTO.ProjectCountResponse;
import com.suza.wasteX.DTO.ProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/projects/count/")
@RequiredArgsConstructor
@Tag(name = "Statistics API", description = "This is Api collection for managing Statistics of  the whole application")
public class ProjectStatisticController {
    private final ProjectService projectService;

    @Operation(
            summary = "get total number of projects",
            description = "This endpoint is for getting total number of projects"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200", description = "project count successfully",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectCountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Project count not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<?> countProjects() {
        Long totalProjects = projectService.countTotalProjects();
        return ResponseEntity.ok(new ProjectCountResponse(totalProjects));
    }
    @Operation(
            summary = "get total number of projects activities",
            description = "This endpoint is for getting total number of project activities per project"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200", description = "project count successfully",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectCountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Project count not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("activities/{projectId}")
    public ResponseEntity<?> countActivitiesByProjectId(@PathVariable Long projectId) {
        long totalActivities = projectService.countActivitiesByProjectId(projectId);
        return ResponseEntity.ok(new ProjectCountResponse(totalActivities));
    }
}
