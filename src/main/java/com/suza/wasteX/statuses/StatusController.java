package com.suza.wasteX.statuses;


import com.suza.wasteX.DTO.StatusDto.StatusRequest;
import com.suza.wasteX.DTO.StatusDto.StatusResponse;
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
@RequestMapping("api/v1/status/")
@RequiredArgsConstructor
@Tag(name = "Status API", description = "This is Api collection for managing Status of the application")
public class StatusController {
    private final StatusService statusService;
    @Operation(
            summary = "create a status",
            description = "This endpoint is for creating status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Status created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusRepository.class))}),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)
    })
    @PostMapping
    ResponseEntity<StatusResponse> createStatus(@RequestBody StatusRequest request) {
        StatusResponse response = statusService.addStatus(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(
            summary = "get list of status",
            description = "This endpoint is for getting a list of status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Status created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusRepository.class))}),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @GetMapping
    ResponseEntity<List<StatusResponse>> getAllStatus() {
        List<StatusResponse> response = statusService.getAllStatuses();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "get a status",
            description = "This endpoint is for a getting status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Status created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusRepository.class))}),
            @ApiResponse(responseCode = "404", description = "status not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @GetMapping("{statusId}")
    ResponseEntity<StatusResponse> getStatusById(@PathVariable Long statusId) {
        StatusResponse response = statusService.getStatusById(statusId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "update a status",
            description = "This endpoint is for update status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Status created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusRepository.class))}),
            @ApiResponse(responseCode = "404", description = "status not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @PutMapping("{statusId}")
    ResponseEntity<StatusResponse> updateStatus(@PathVariable Long statusId, @RequestBody StatusRequest request) {
        StatusResponse response = statusService.updateStatus(statusId, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();

    }
    @Operation(
            summary = "delete a status",
            description = "This endpoint is for deleting status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Status created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusRepository.class))}),
            @ApiResponse(responseCode = "404", description = "status not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @DeleteMapping("{statusId}")
    ResponseEntity<Void> deleteStatus(@PathVariable Long statusId) {
        Boolean response = statusService.deleteStatus(statusId);
        return response ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();


    }


}
