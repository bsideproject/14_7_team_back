package com.mineservice.global.infra.object_storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mineservice.global.config.ObjectStorageConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ObjectStorageService {

    private final AmazonS3 amazonS3;

    private final ObjectStorageConfig objectStorageConfig;


    public void uploadMultipartFile(MultipartFile file, String bucketPath) {
        try {
            upload(file, bucketPath);
        } catch (IOException e) {
            log.error("file upload fail {}", e.getMessage());
        }
    }


    /**
     * MultipartFile 업로드
     * @param multipartFile the multipartFile
     * @param bucketPath    버킷 저장위치
     * @throws IOException the io exception
     */
    private void upload(MultipartFile multipartFile, String bucketPath) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getBytes().length);
        metadata.setContentType("application/x-directory");
        amazonS3.putObject(new PutObjectRequest(objectStorageConfig.getBucket(), bucketPath, multipartFile.getInputStream(), metadata));
    }

    /**
     * byte[] 업로드
     * @param bytes      the bytes
     * @param bucketPath 버킷 저장위치
     */
    private void upload(byte[] bytes, String bucketPath) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        metadata.setContentType("application/x-directory");
        amazonS3.putObject(new PutObjectRequest(objectStorageConfig.getBucket(), bucketPath, new ByteArrayInputStream(bytes), metadata));
    }


    public byte[] download(String bucketPath) {
        try {
            return amazonS3.getObject(objectStorageConfig.getBucket(), bucketPath).getObjectContent().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
