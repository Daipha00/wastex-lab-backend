package com.suza.wasteX.projectType;

import com.suza.wasteX.DTO.TypeRequest;
import com.suza.wasteX.DTO.TypeResponse;

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
import java.util.Map;

@RestController
@RequestMapping("api/v1/types/")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Tag(name = "Project Type API", description = "This is Api collection for managing Project Type")
public class TypeController {
    private final TypeService typeService;

    @Operation(
            summary = "create an type",
            description = "This endpoint is for creating an type"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Type created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TypeResponse.class))}),
            @ApiResponse(responseCode = "400" , description = "Bad Request for type information", content = @Content)
    })
    @PostMapping
    ResponseEntity<TypeResponse> createType(@RequestBody TypeRequest request) {
        TypeResponse response = typeService.createType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(
            summary = "assign an project type",
            description = "This endpoint is for assign project type"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project type created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TypeResponse.class))}),
            @ApiResponse(responseCode = "400" , description = "Bad Request for Project type information", content = @Content),
            @ApiResponse(responseCode = "404" , description = "Project id not found", content = @Content)

    })
    @PostMapping("{projectId}")
    ResponseEntity<TypeResponse> createProjectType(@PathVariable Long projectId, @RequestBody TypeRequest request) {
        TypeResponse response = typeService.addProjectType(projectId,request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    @Operation(
            summary = "get list of  an type",
            description = "This endpoint is for getting list of an type"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Type created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TypeResponse.class))}),
            @ApiResponse(responseCode = "400" , description = "Bad Request for type information", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<TypeResponse>> getAllTypes() {
        List<TypeResponse> response = typeService.getAllTypes();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Get project counts by type",
            description = "This endpoint counts how many projects exist for specific types (hackathon, incubation)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Counts retrieved successfully",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("counts")
    public ResponseEntity<Map<String, Long>> getProjectTypeCounts() {
        Map<String, Long> counts = typeService.getProjectTypeCounts();
        return ResponseEntity.ok(counts);
    }

    @Operation(
            summary = "Get total number of project types",
            description = "This endpoint returns the total number of all registered project types"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Total count retrieved successfully",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("total-count")
    public ResponseEntity<Long> getTotalTypesCount() {
        Long totalCount = typeService.getTotalTypesCount();
        return ResponseEntity.ok(totalCount);
    }


    @Operation(
            summary = "get an type",
            description = "This endpoint is for getting an type"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Type created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TypeResponse.class))}),
            @ApiResponse(responseCode = "404" , description = "Type is not found", content = @Content)
    })
    @GetMapping("{typeId}")
    ResponseEntity<TypeResponse> getTypeById(@PathVariable Long typeId) {
        TypeResponse response = typeService.getTypeById(typeId);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    @Operation(
            summary = "update an type",
            description = "This endpoint is for updating an type"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Type created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TypeResponse.class))}),
            @ApiResponse(responseCode = "404" , description = "Type is not found", content = @Content)
    })
    @PutMapping("{typeId}")
    ResponseEntity<TypeResponse> updateType(@PathVariable Long typeId, @RequestBody TypeRequest request) {
        TypeResponse response = typeService.updateType(typeId, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();

    }

    @Operation(
            summary = "delete an type",
            description = "This endpoint is for  deleting an type"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Type deleted successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TypeResponse.class))}),
            @ApiResponse(responseCode = "404" , description = "Type is not found", content = @Content)
    })
    @DeleteMapping("{typeId}")
    ResponseEntity<Void> deleteType(@PathVariable Long typeId) {
        Boolean response = typeService.deleteType(typeId);
        return response ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();


    }

}
