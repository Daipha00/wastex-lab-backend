package com.suza.wasteX.startup;

import com.suza.wasteX.DTO.StartupRequest;
import com.suza.wasteX.DTO.StartupResponse;
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
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/startups/")
@Tag(name = "Startup API", description = "This is API Collection for Manage Startup")
public class StartupController {
    private final StartupService startUpService;

    @Operation(
            summary = "create a startup",
            description = "This endpoint is for add new a startup company"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "201",description = "Startup created successful", content = {
                    @Content(mediaType = "application/json",
                             schema = @Schema(implementation = StartupResponse.class)) }),

    })
    @PostMapping
    ResponseEntity<StartupResponse> createStartUp(@RequestBody StartupRequest request) {
        StartupResponse response = startUpService.createStartUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "get a list of startup information",
            description = "This endpoint is for getting the List of startup information"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "201",description = "Startup created successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StartupResponse.class)) }),

            @ApiResponse(responseCode = "500",description = "Server Error", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<StartupResponse>> getAllStartUp() {
        List<StartupResponse> response = startUpService.getAllStartUp();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Operation(
            summary = "get a startup information by Name",
            description = "This endpoint is for getting a startup information using startup's registered name"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200",description = "Startup created successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StartupResponse.class)) }),

            @ApiResponse(responseCode = "404",description = "Startup not Found", content = @Content)
    })
    @GetMapping("{startup}")
    ResponseEntity<Optional<StartupResponse>> getStartupByName(@PathVariable String startup) {
        Optional<StartupResponse> response = startUpService.getStartupByName(startup);
        if (response.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            summary = "update a Startup",
            description = "This endpoint is for update an existing startup"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200",description = "Startup created successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StartupResponse.class)) }),

            @ApiResponse(responseCode = "404",description = "Startup not Found", content = @Content)
    })
    @PutMapping("{startupId}")
    ResponseEntity<StartupResponse> updateStartUp(@PathVariable Long startupId, @RequestBody StartupRequest request) {
        StartupResponse response = startUpService.updateStartup(startupId,request);
        if(response != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }
    @Operation(
            summary = "delete a startup",
            description = "This endpoint is for delete a startup"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "204",description = "Startup deleted successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StartupResponse.class)) }),

            @ApiResponse(responseCode = "404",description = "Startup not Found", content = @Content)
    })
    @DeleteMapping("{startupId}")
    public ResponseEntity<StartupResponse> deleteStartup(@PathVariable Long startupId) {
        boolean startup = startUpService.deleteStartup(startupId);
        if(startup) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }
}
