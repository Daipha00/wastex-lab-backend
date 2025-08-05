package com.suza.wasteX.projectActivity;

import com.opencsv.exceptions.CsvException;
import com.suza.wasteX.DTO.*;
import com.suza.wasteX.DTO.StatusDto.ActivityStatusRequest;
import com.suza.wasteX.DTO.StatusDto.ProjectStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/activities/")
@RequiredArgsConstructor
@Tag(name = "Project Activity API", description = "This is Api collection for managing Project activities")
public class ActivityController {
    private final ActivityService activityService;

    @Operation(
            summary = "create a new activity",
            description = "This endpoint is for creating a new activity under a given project"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Activity created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @PostMapping("/{projectId}")
    public ResponseEntity<ActivityResponse> createActivity(
            @PathVariable Long projectId,
            @RequestBody ActivityRequest request) {
        ActivityResponse response = activityService.createActivity(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "get list of  an activity",
            description = "This endpoint is for getting list of an activity"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Activity created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityResponse.class))}),
            @ApiResponse(responseCode = "400" , description = "Bad Request for activity information", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<ActivityResponse>> getAllActivities() {
        List<ActivityResponse> response = activityService.getAllActivities();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Operation(
            summary = "get an activity",
            description = "This endpoint is for getting an activity"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activity created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityResponse.class))}),
            @ApiResponse(responseCode = "404" , description = "Activity is not found", content = @Content)
    })
    @GetMapping("{activityId}")
    ResponseEntity<ActivityResponse> getActivityById(@PathVariable Long activityId) {
        ActivityResponse response = activityService.getActivityById(activityId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Operation(
            summary = "update an activity",
            description = "This endpoint is for updating an activity"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activity created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityResponse.class))}),
            @ApiResponse(responseCode = "404" , description = "Activity is not found", content = @Content)
    })
    @PutMapping("/{projectId}/{activityId}")
    public ResponseEntity<ActivityResponse> updateProjectActivity(@PathVariable Long projectId,@PathVariable Long activityId, @RequestBody ActivityRequest request){
        ActivityResponse response = activityService.updateActivity(projectId, activityId,request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "delete an activity",
            description = "This endpoint is for  deleting an activity"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Activity deleted successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityResponse.class))}),
            @ApiResponse(responseCode = "404" , description = "Activity is not found", content = @Content)
    })
    @DeleteMapping("{activityId}")
    ResponseEntity<Void> deleteActivity(@PathVariable Long activityId) {
        Boolean response = activityService.deleteActivity(activityId);
        return response ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

//    Business Logic
@Operation(
        summary = "add new activity status",
        description = "This endpoint is for add status to Activity"
)
@ApiResponses( {
        @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = ActivityResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
})
@PostMapping("status/{id}")
public ResponseEntity<ActivityResponse> addActivityStatus(@PathVariable Long id , @RequestBody ActivityStatusRequest request) {
    ActivityResponse response = activityService.addActivityStatus(id,request);
    return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();

}
@Operation(
        summary = "add a activity sponsor to any existing Activity",
        description = "This endpoint is for add an existing  Activity with sponsor (append the sponsor)"
)
@ApiResponses( {
        @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = ActivityResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Activity not found", content = @Content)
})
@PostMapping("/{activityId}/sponsors")
public ResponseEntity<ActivityResponse> addActivitySponsors(
        @PathVariable Long activityId,
        @RequestBody List<Long> sponsorIds) {
    ActivityResponse response = activityService.addSponsorsToActivity(activityId, sponsorIds);
    return ResponseEntity.ok(response);
}
    @Operation(
            summary = "update all Activity sponsor",
            description = "This endpoint is for update all an existing  Activity sponsor and add new ones (replacing the existing ones)"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ActivityResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Activity not found", content = @Content)
    })
    @PutMapping("/{projectId}/sponsors")
    public ResponseEntity<ActivityResponse> updateActivitySponsors(
            @PathVariable Long projectId,
            @RequestBody List<Long> sponsorIds) {
        ActivityResponse response = activityService.updateActivitySponsors(projectId, sponsorIds);
        return ResponseEntity.ok(response);
    }
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Members uploaded successfully", content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = MemberResponse.class))}),
        @ApiResponse(responseCode = "404" , description = "Activity not found", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid request or CSV format", content = @Content)
})
@PostMapping(value = "/{activityId}/members", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public ResponseEntity<List<MemberResponse>> uploadMembers(
        @PathVariable Long activityId,
        @RequestParam("file") MultipartFile file) {
    try {
        List<MemberResponse> members = activityService.uploadMembers(activityId, file);
        return ResponseEntity.ok(members);
    } catch (IOException | CsvException e) {
        return ResponseEntity.badRequest().build();
    }
}

}
