package com.mineservice.global.infra.object_storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mineservice.global.config.ObjectStorageConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ObjectStorageService {

    private final AmazonS3 amazonS3;

    private final ObjectStorageConfig objectStorageConfig;

    public void uploadMultipartFile(Long articleId, MultipartFile file) {
        String uuid = UUID.randomUUID().toString(); // uuid
        String fileExt = FilenameUtils.getExtension(file.getOriginalFilename()); // 확장자
        String savePath = fileExt + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM")); // 저장경로
        String fileName = uuid + "." + fileExt; // 저장파일명
        String bucketPath = savePath + "/" + fileName; // 버킷경로

        try {
            upload(file, bucketPath);
            // todo file_info 테이블 저장
        } catch (IOException e) {
            e.printStackTrace();
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
