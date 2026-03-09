package com.suza.wasteX.project;

import com.suza.wasteX.DTO.*;
import com.suza.wasteX.DTO.StatusDto.ProjectStatusRequest;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Project API", description = "This is API Collection for Manage Project")
public class ProjectController {
    private final ProjectService projectService;

    @Operation(
            summary = "create a project",
            description = "This endpoint is for add new project"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "201", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "get a list of project",
            description = "This endpoint is for getting the list of projects"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> responses = projectService.getAllProjects();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @Operation(
            summary = "get a project",
            description = "This endpoint is for getting a project"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        ProjectResponse response = projectService.getProjectById(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "delete a project",
            description = "This endpoint is for delete a project"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "204", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        boolean response = projectService.deleteProject(id);
        return response  ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }


//   The business Logic for the project sponsor, activity and project status
@Operation(
        summary = "add new project status",
        description = "This endpoint is for add status to project"
)
@ApiResponses( {
        @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProjectResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
})
@PostMapping("status/{id}")
public ResponseEntity<ProjectResponse> addProjectStatus(@PathVariable Long id , @RequestBody ProjectStatusRequest request) {
    ProjectResponse response = projectService.addProjectStatus(id,request);
    return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();

}
@Operation(
        summary = "add a project sponsor to any existing project",
        description = "This endpoint is for add an existing  project with sponsor (append the sponsor)"
)
@ApiResponses( {
        @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProjectResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
})
@PostMapping("{projectId}/sponsors")
public ResponseEntity<ProjectResponse> addProjectSponsors(
        @PathVariable Long projectId,
        @RequestBody List<Long> sponsorIds) {
    ProjectResponse response = projectService.addSponsorsToProject(projectId, sponsorIds);
    return ResponseEntity.ok(response);
}
    @Operation(
            summary = "update all project sponsor",
            description = "This endpoint is for update all an existing  project sponsor and add new ones (replacing the existing ones)"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @PutMapping("{projectId}/sponsors")
    public ResponseEntity<ProjectResponse> updateProjectSponsors(
            @PathVariable Long projectId,
            @RequestBody List<String> sponsorNames) {

        ProjectResponse response =
                projectService.updateProjectSponsors(projectId, sponsorNames);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "add new project activity",
            description = "This endpoint is for add new project activity"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201" , description = "Project activity added successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Project Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Damn!! Internal Server Error", content = @Content)
    })
    @PostMapping("{projectId}/activities")
    public ResponseEntity<ProjectResponse> addActivityToProject(@PathVariable Long projectId, @RequestBody ActivityRequest activityRequest) {
        ProjectResponse response = projectService.addActivityToProject(projectId, activityRequest);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.updateProject(id, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }


}
