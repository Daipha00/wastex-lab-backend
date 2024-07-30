package com.suza.wasteX.fileUploads;

import com.suza.wasteX.customException.NotFoundException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class FileUploadService {
    private final MinioClient minioClient;
    private final String bucketName;

    @Value("${minio.url}") String ulr;


    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB



    public FileUploadService(@Value("${minio.url}") String url,
                             @Value("${minio.access-key}") String accessKey,
                             @Value("${minio.secret-key}") String secretKey,
                             @Value("${minio.bucket-name}") String bucketName) {

        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucketName;

    }

    public String uploadImg(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            InputStream fileInputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(fileInputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to MinIO", e);
        }
    }

    public String uploadFile(String folderPath, MultipartFile file) throws Exception {


        if (file.getSize() > MAX_FILE_SIZE) {
            throw new NotFoundException("File size exceeds the maximum allowed size of 2MB");
        }
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;
        String objectName = folderPath + "/" + uniqueFileName;  // Create the hierarchical structure
        InputStream inputStream = file.getInputStream();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return objectName;
    }

    public String getFileUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .method(Method.GET)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException |
                 NoSuchAlgorithmException | XmlParserException | ServerException e) {
            throw new RuntimeException("Error generating presigned URL", e);
        }
    }

    public InputStream downloadFile(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    public void deleteFile(String objectName) throws Exception {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                throw new RuntimeException("File not found");
            }
            throw e;
        }
    }

    public Boolean fileExists(String objectName) throws Exception {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw e;
        }
    }
}
