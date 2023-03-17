package com.mineservice.global.infra.object_storage;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.*;

@SpringBootTest
class ObjectStorageServiceTest {

    @Autowired
    private ObjectStorageService objectStorageService;

    @Test
    void uploadTest() throws IOException {

        String filePath = "D:\\logo.png";
        File file = new File(filePath);
        String multipartFileName = file.getName();
        String fileExt = FilenameUtils.getExtension(file.getName());

        MockMultipartFile multipartFile = new MockMultipartFile(multipartFileName, multipartFileName + "." + fileExt, fileExt, new FileInputStream(filePath));

        objectStorageService.uploadMultipartFile(0L, multipartFile);

    }

    @Test
    void downloadTest() throws IOException {
        String bucketPath = "png/2023/03/bd58b1c7-e362-49c8-82e8-dcfac2e0b3a0.png"; // 버킷경로

        byte[] download = objectStorageService.download(bucketPath);


        // 파일로 저장 테스트
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = new FileOutputStream("D:\\logo2.png");
        baos.write(download, 0, download.length);
        fos.write(baos.toByteArray());
        baos.flush();
        baos.close();
        fos.close();
    }


}