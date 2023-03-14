package com.mineservice.domain.article.api;

import com.mineservice.domain.article.dto.ArticleReqDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ArticleController {

    public static List<ArticleReqDTO> dummyList = new ArrayList<>();

    @PostMapping(value = "/article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "아티클 저장")
    public ResponseEntity<String> registerArticle(@ModelAttribute ArticleReqDTO reqDTO) {
        log.info("requestDTO :{}", reqDTO.toString());
        log.info("imgOrgName :{}", reqDTO.getImg().getOriginalFilename());

        // TODO: 2023-03-14(014) image 핸들링
        dummyList.add(reqDTO);

        return ResponseEntity.ok("success");
    }

    @GetMapping("/article")
    @ApiOperation(value = "아티클 불러오기")
    public ResponseEntity<String> getArticles() {
        log.info("responseBody :{}", dummyList.toString());
        return ResponseEntity.ok(dummyList.toString());
    }


}
