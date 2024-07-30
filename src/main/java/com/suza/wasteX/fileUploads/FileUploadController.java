package com.suza.wasteX.fileUploads;

import io.minio.errors.ErrorResponseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/file/")
@Tag(name = "File Uploads API", description = "This is Api collection for managing file uploads like uploading file , downloading (using minIO)")
public class FileUploadController {
    private final FileUploadService fileUploadService;
    @Operation(summary = "Upload a file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File uploaded successfully", content = {
                    @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping(value = "upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> uploadFile(@RequestParam("folder") String folderPath, @RequestParam("file") MultipartFile file) {
        try {
            String objectName = fileUploadService.uploadFile(folderPath, file);
            return ResponseEntity.ok(objectName);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }

    @Operation(summary = "Download a file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File downloaded successfully", content = {
                    @Content(mediaType = "application/octet-stream")}),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("objectName") String objectName) {
        try {
            if (!fileUploadService.fileExists(objectName)) {
                return ResponseEntity.status(404).body(null);
            }
            InputStream inputStream = fileUploadService.downloadFile(objectName);
            byte[] bytes = inputStream.readAllBytes();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "View a file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File retrieved successfully", content = {
                    @Content(mediaType = "application/octet-stream")}),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/view")
    public ResponseEntity<byte[]> viewFile(@RequestParam("objectName") String objectName) {
        try {
            if (!fileUploadService.fileExists(objectName)) {
                return ResponseEntity.status(404).body(null);
            }
            InputStream inputStream = fileUploadService.downloadFile(objectName);
            byte[] bytes = inputStream.readAllBytes();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + objectName + "\"")
                    .body(bytes);
        } catch (Exception e) {
            if (e instanceof ErrorResponseException && ((ErrorResponseException) e).errorResponse().code().equals("NoSuchKey")) {
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Delete a file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("objectName") String objectName) {
        try {
            fileUploadService.deleteFile(objectName);
            return ResponseEntity.ok("File deleted successfully");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("File not found")) {
                return ResponseEntity.status(404).body("File not found");
            }
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
}
