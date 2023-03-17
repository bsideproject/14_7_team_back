package com.mineservice.domain.article.api;

import com.mineservice.domain.article.dto.ArticleReqDTO;
import com.mineservice.global.common.response.CommonResponse;
import com.mineservice.global.common.response.ResponseService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

    public static List<ArticleReqDTO> dummyList = new ArrayList<>();

    private final ResponseService responseService;

    @PostMapping(value = "/article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "아티클 저장")
    public CommonResponse registerArticle(@ModelAttribute ArticleReqDTO reqDTO) {
        log.info("requestDTO :{}", reqDTO.toString());
        log.info("imgOrgName :{}", reqDTO.getImg().getOriginalFilename());

        // TODO: 2023-03-14(014) image 핸들링
        dummyList.add(reqDTO);

        return responseService.getSuccessResponse();
    }

    @GetMapping("/article")
    @ApiOperation(value = "아티클 불러오기")
    public CommonResponse getArticles() {
        log.info("responseBody :{}", dummyList.toString());

        return responseService.getListResponse(dummyList);
    }

    @DeleteMapping("/article/{id}")
    @ApiOperation(value = "아티클 삭제")
    public CommonResponse deleteArticle(@PathVariable Long id) {
        log.info("deleteArticle id :{}", id);

        return responseService.getSuccessResponse();
    }


}
