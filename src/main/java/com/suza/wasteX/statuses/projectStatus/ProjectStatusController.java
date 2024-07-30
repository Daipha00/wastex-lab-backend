package com.suza.wasteX.statuses.projectStatus;

import com.suza.wasteX.DTO.*;
import com.suza.wasteX.DTO.StatusDto.ProjectStatusRequest;
import com.suza.wasteX.DTO.StatusDto.ProjectStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/project_statuses/")
@Tag(name = "Status API", description = "This is API collect for managing Project Status")
public class ProjectStatusController {
    private final ProjectStatusService projectStatusService;

    @Operation(
            summary = "create a project status",
            description = "This endpoint is for creating project status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "ProjectStatus created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectStatusResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)
    })
    @PostMapping
    ResponseEntity<ProjectStatusResponse> createProjectStatus(@RequestBody ProjectStatusRequest request) {
        ProjectStatusResponse response = projectStatusService.addProjectStatus(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(
            summary = "get a List of project status",
            description = "This endpoint is for getting list of project status"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200",description = "Project status OK ", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectStatusResponse.class))}),
    })
    @GetMapping
    ResponseEntity<List<ProjectStatusResponse>> getAllProjectStatus() {
        List<ProjectStatusResponse> responses = projectStatusService.getAllProjectStatuses();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @Operation(
            summary = "get a project status",
            description = "This endpoint is for getting a project status"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200", description = "Project status Ok", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectStatusResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Project status not found", content = @Content)
    })
    @GetMapping("{statusId}")
    ResponseEntity<ProjectStatusResponse> getProjectStatusById(@PathVariable Long statusId) {
        ProjectStatusResponse response = projectStatusService.getProjectStatus(statusId);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "delete a project status",
            description = "This endpoint is for delete a particular project status"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "204", description = "Project status with No Content ", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectStatusResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Project status is not found", content = @Content)
    })
    @DeleteMapping("{statusId}")
    ResponseEntity<Void> deleteProjectStatus(@PathVariable Long statusId) {
        boolean response = projectStatusService.deleteProjectStatus(statusId);
        return response? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @Operation(summary = "Update project status",
            description = "This endpoint is for add new project activity"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201" , description = "Update Project status successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Project or Project Status Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Damn!! Internal Server Error", content = @Content)
    })
    @PutMapping("{projectId}/statuses/{statusId}")
    public ResponseEntity<ProjectResponse> updateProjectStatus(@PathVariable Long projectId,@PathVariable Long statusId, @RequestBody ProjectStatusRequest statusRequest) {
        ProjectResponse response = projectStatusService.updateProjectStatus(projectId, statusId, statusRequest);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
}
