package com.suza.wasteX.sponsor;

import com.suza.wasteX.DTO.SponsorRequest;
import com.suza.wasteX.DTO.SponsorResponse;
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
@RequestMapping("api/v1/sponsor")
@RequiredArgsConstructor
@Tag(name = "Sponsor API", description = "This is Api collection for managing Sponsor")
public class SponsorController {
    private final SponsorService sponsorService;
    @Operation(
            summary = "create a sponsor",
            description = "This endpoint is for creating sponsor"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Sponsor created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SponsorRepository.class))}),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)
    })
    @PostMapping
    ResponseEntity<SponsorResponse> createSponsor(@RequestBody SponsorRequest request) {
        SponsorResponse response = sponsorService.addSponsor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(
            summary = "get list of sponsor",
            description = "This endpoint is for getting a list of sponsor"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Sponsor created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SponsorRepository.class))}),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @GetMapping
    ResponseEntity<List<SponsorResponse>> getAllSponsor() {
        List<SponsorResponse> response = sponsorService.getAllSponsors();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "get a sponsor",
            description = "This endpoint is for a getting sponsor"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Sponsor created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SponsorRepository.class))}),
            @ApiResponse(responseCode = "404", description = "sponsor not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @GetMapping("{sponsorId}")
    ResponseEntity<SponsorResponse> getSponsorById(@PathVariable Long sponsorId) {
        SponsorResponse response = sponsorService.getSponsorById(sponsorId);
        return response!= null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "update a sponsor",
            description = "This endpoint is for update sponsor"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Sponsor created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SponsorRepository.class))}),
            @ApiResponse(responseCode = "404", description = "sponsor not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @PutMapping("{sponsorId}")
    ResponseEntity<SponsorResponse> updateSponsor(@PathVariable Long sponsorId, @RequestBody SponsorRequest request) {
        SponsorResponse response = sponsorService.updateSponsor(sponsorId, request);
        return response!= null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();

    }
    @Operation(
            summary = "delete a sponsor",
            description = "This endpoint is for deleting sponsor"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200" , description = "Sponsor created successfully", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SponsorRepository.class))}),
            @ApiResponse(responseCode = "404", description = "sponsor not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)

    })
    @DeleteMapping("{sponsorId}")
    ResponseEntity<Void> deleteSponsor(@PathVariable Long sponsorId) {
        Boolean response = sponsorService.deleteSponsor(sponsorId);
        return response ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }

}
