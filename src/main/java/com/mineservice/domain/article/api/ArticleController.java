package com.mineservice.domain.article.api;

import com.mineservice.domain.article.dto.ArticleReqDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ArticleController {

    public static List<ArticleReqDTO> dummyList = new ArrayList<>();

    @PostMapping("/article")
    @ApiOperation(value = "링크 게시글 저장 API")
    public ResponseEntity<String> registerArticle(@RequestBody ArticleReqDTO reqDTO) {

        dummyList.add(reqDTO);

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    @GetMapping("/article")
    public ResponseEntity<List<ArticleReqDTO>> getArticles() {

        return ResponseEntity.ok(dummyList);
    }


}
