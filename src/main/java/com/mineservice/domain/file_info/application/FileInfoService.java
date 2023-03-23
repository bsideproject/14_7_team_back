package com.mineservice.domain.file_info.application;

import com.mineservice.domain.file_info.domain.FileInfo;
import com.mineservice.domain.file_info.repository.FileInfoRepository;
import com.mineservice.global.config.ObjectStorageConfig;
import com.mineservice.global.infra.object_storage.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileInfoService {

    private final ObjectStorageService objectStorageService;
    private final ObjectStorageConfig objectStorageConfig;
    private final FileInfoRepository fileInfoRepository;

    @Transactional
    public void createFileInfo(String userId, Long articleId, MultipartFile file) {
        String uuid = UUID.randomUUID().toString(); // uuid
        String fileExt = FilenameUtils.getExtension(file.getOriginalFilename()); // 확장자
        String savePath = fileExt + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM")); // 저장경로
        String fileName = uuid + "." + fileExt; // 저장파일명
        String bucketPath = savePath + "/" + fileName; // 버킷경로

        FileInfo fileInfo = FileInfo.builder()
                .articleId(articleId)
                .bucketName(objectStorageConfig.getBucket())
                .filePath(bucketPath)
                .fileExt(fileExt)
                .saveName(fileName)
                .orgName(file.getOriginalFilename())
                .createBy(userId)
                .build();

        objectStorageService.uploadMultipartFile(file, bucketPath);

        fileInfoRepository.save(fileInfo);
        log.info("fileInfo: {}", fileInfo.toString());
    }

}
