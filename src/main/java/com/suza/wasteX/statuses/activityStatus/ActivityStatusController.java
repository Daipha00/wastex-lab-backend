package com.suza.wasteX.statuses.activityStatus;

import com.suza.wasteX.DTO.*;
import com.suza.wasteX.DTO.StatusDto.ActivityStatusRequest;
import com.suza.wasteX.DTO.StatusDto.ActivityStatusResponse;
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
@RequestMapping("/api/v1/activity_statuses/")
@Tag(name = "Status API", description = "This is API Collection for Manage Activity status")
public class ActivityStatusController {
    private final ActivityStatusService activityStatusService;

    @Operation(
            summary = "create a activityStatus status",
            description = "This endpoint is for creating activityStatus status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "ActivityStatusStatus created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityStatusResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)
    })
    @PostMapping
    ResponseEntity<ActivityStatusResponse> createActivityStatus(@RequestBody ActivityStatusRequest request) {
        ActivityStatusResponse response = activityStatusService.addActivityStatus(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "get a list of activity status",
            description = "This endpoint is for get list of statuses for any of activity"
    )
    @GetMapping
    ResponseEntity<List<ActivityStatusResponse>> getAllActivityStatuses() {
        List<ActivityStatusResponse> responses = activityStatusService.getAllActivityStatuses();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
    @Operation(
            summary = "get a activity status",
            description = "This endpoint for getting a status for an activity"
    )
    @GetMapping("{statusId}")
    ResponseEntity<ActivityStatusResponse> getActivityStatusById(@PathVariable Long statusId) {
        ActivityStatusResponse response = activityStatusService.getActivityStatusById(statusId);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }


    @Operation(
            summary = "delete a activity status",
            description = "This endpoint is for delete a particular activity status"
    )
    @DeleteMapping("{statusId}")
    ResponseEntity<Void> deleteActivityStatus(@PathVariable Long statusId) {
        Boolean response = activityStatusService.deleteActivityStatus(statusId);
        return response ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }

    @Operation(summary = "Update activity status",
            description = "This endpoint is for update activity status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201" , description = "Update Activity status successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Activity or Activity Status Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Damn!! Internal Server Error", content = @Content)
    })
    @PutMapping("/{activityId}/statuses/{statusId}")
    public ResponseEntity<ActivityResponse> updateActivityStatus(@PathVariable Long activityId, @PathVariable Long statusId, @RequestBody ActivityStatusRequest statusRequest) {
        ActivityResponse status = activityStatusService.updateActivityStatus(activityId, statusId, statusRequest);
        return status != null ? ResponseEntity.ok(status) : ResponseEntity.notFound().build();
    }



}
