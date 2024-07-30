package com.suza.wasteX.institution;

import com.suza.wasteX.DTO.InstitutionRequest;
import com.suza.wasteX.DTO.InstitutionResponse;
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
@RequestMapping("api/v1/institutions/")
@RequiredArgsConstructor
@Tag(name = "Institution API", description = "This is API collection for managing Institution")
public class InstitutionController {
    private final InstitutionService institutionService;

    @Operation(
            summary = "create an institution",
            description = "This endpoint is for creating an institution"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Institution created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionResponse.class))}),
            @ApiResponse(responseCode = "400" , description = "Bad Request for institution information", content = @Content)
    })
    @PostMapping
    ResponseEntity<InstitutionResponse> createInstitution(@RequestBody InstitutionRequest request) {
        InstitutionResponse response = institutionService.createInstitution(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(
            summary = "get list of  an institution",
            description = "This endpoint is for getting list of an institution"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Institution created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionResponse.class))}),
            @ApiResponse(responseCode = "400" , description = "Bad Request for institution information", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<InstitutionResponse>> getAllInstitutions() {
        List<InstitutionResponse> response = institutionService.getAllInstitutions();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Operation(
            summary = "get an institution",
            description = "This endpoint is for getting an institution"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Institution created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionResponse.class))}),
            @ApiResponse(responseCode = "404" , description = "Institution is not found", content = @Content)
    })
    @GetMapping("{institutionId}")
    ResponseEntity<InstitutionResponse> getInstitutionById(@PathVariable Long institutionId) {
        InstitutionResponse response = institutionService.getInstitutionById(institutionId);
        return response!= null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    @Operation(
            summary = "update an institution",
            description = "This endpoint is for updating an institution"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Institution created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionResponse.class))}),
            @ApiResponse(responseCode = "404" , description = "Institution is not found", content = @Content)
    })
    @PutMapping("{institutionId}")
    ResponseEntity<InstitutionResponse> updateInstitution(@PathVariable Long institutionId, @RequestBody InstitutionRequest request) {
        InstitutionResponse response = institutionService.updateInstitution(institutionId, request);
        return response!= null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "delete an institution",
            description = "This endpoint is for  deleting an institution"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Institution deleted successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionResponse.class))}),
            @ApiResponse(responseCode = "404" , description = "Institution is not found", content = @Content)
    })
    @DeleteMapping("{institutionId}")
    ResponseEntity<Void> deleteInstitution(@PathVariable Long institutionId) {
        Boolean response = institutionService.deleteInstitution(institutionId);
        return response ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
