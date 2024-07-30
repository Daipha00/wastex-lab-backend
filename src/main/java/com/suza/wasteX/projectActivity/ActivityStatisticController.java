package com.suza.wasteX.projectActivity;

import com.suza.wasteX.DTO.ProjectCountResponse;
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
@RequestMapping("api/v1/activity/counts")
@RequiredArgsConstructor
@Tag(name = "Statistics API")
public class ActivityStatisticController {
    private final ActivityService activityService;

    @Operation(
            summary = "get total number of activities status",
            description = "This endpoint is for getting total number of activities status per Activity"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200", description = "Status count successfully",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProjectCountResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Status count not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("status/{activityId}")
    public ResponseEntity<?> countActivityStatus(@PathVariable Long activityId) {
        long totalActivities = activityService.countActivityStatusByActivity(activityId);
        return ResponseEntity.ok(new ProjectCountResponse(totalActivities));
    }
}
