package com.mineservice.domain.file_info.controller;

import com.mineservice.domain.file_info.application.FileInfoService;
import com.mineservice.domain.file_info.domain.FileInfoEntity;
import com.mineservice.global.infra.object_storage.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FileInfoController {

    private final FileInfoService fileInfoService;
    private final ObjectStorageService objectStorageService;

    @GetMapping("/image/thumb/{article_id}")
    public ResponseEntity<byte[]> thumbImage(@PathVariable("article_id") Long articleId, HttpServletResponse response) {
        log.info("articleId : {}", articleId);
        FileInfoEntity fileInfo = fileInfoService.findOneByArticleId(articleId);
        if (fileInfo == null) {
            return ResponseEntity.badRequest().build();
        }

        byte[] bytes = objectStorageService.download(fileInfo.getFilePath());

        MediaType mediaType = getMediaTypeForImage(fileInfo.getFileExt());
        if (mediaType == null) {
            return ResponseEntity.badRequest().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    private MediaType getMediaTypeForImage(String fileExtension) {
        if (fileExtension.equalsIgnoreCase("png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileExtension.equalsIgnoreCase("gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return null;
        }
    }


}
