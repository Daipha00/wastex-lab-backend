package com.suza.wasteX.wastex_site_api.API;


import com.suza.wasteX.DTO.WebsiteDTO.FeatureRequest;
import com.suza.wasteX.DTO.WebsiteDTO.FeatureResponse;
import com.suza.wasteX.wastex_site_api.services.FeatureService;
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
@RequestMapping("api/v1/features")
@RequiredArgsConstructor
@Tag(name = "WasteX Website API", description = "This is collection of API for managing WasteX Website")
public class FeatureController {
    private final FeatureService featureService;

    @Operation(
            summary = "create a feature",
            description = "This endpoint is for add new a feature company"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "201",description = "Feature created successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeatureResponse.class)) }),

    })
    @PostMapping
    ResponseEntity<FeatureResponse> createFeature(@RequestBody FeatureRequest request) {
        FeatureResponse response = featureService.createFeature(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "get a list of feature information",
            description = "This endpoint is for getting the List of feature information"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "201",description = "Feature created successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeatureResponse.class)) }),

            @ApiResponse(responseCode = "500",description = "Server Error", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<FeatureResponse>> getAllFeature() {
        List<FeatureResponse> response = featureService.getAllFeature();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "update a Feature",
            description = "This endpoint is for update an existing feature"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "200",description = "Feature created successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeatureResponse.class)) }),

            @ApiResponse(responseCode = "404",description = "Feature not Found", content = @Content)
    })
    @PutMapping("{featureId}")
    ResponseEntity<FeatureResponse> updateFeature(@PathVariable Long featureId, @RequestBody FeatureRequest request) {
        FeatureResponse response = featureService.updateFeature(featureId,request);
        if(response != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }
    @Operation(
            summary = "delete a feature",
            description = "This endpoint is for delete a feature"
    )
    @ApiResponses( {
            @ApiResponse(responseCode = "204",description = "Feature deleted successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeatureResponse.class)) }),

            @ApiResponse(responseCode = "404",description = "Feature not Found", content = @Content)
    })
    @DeleteMapping("{featureId}")
    public ResponseEntity<FeatureResponse> deleteFeature(@PathVariable Long featureId) {
        boolean feature = featureService.deleteFeature(featureId);
        if(feature) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }
}
