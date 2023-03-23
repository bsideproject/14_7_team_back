package com.mineservice.domain.article.api;

import com.mineservice.domain.article.application.ArticleService;
import com.mineservice.domain.article.dto.ArticleReqDTO;
import com.mineservice.domain.article.dto.ArticleResDTO;
import com.mineservice.domain.user.domain.UserInfo;
import com.mineservice.global.common.response.CommonResponse;
import com.mineservice.global.common.response.ResponseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ResponseService responseService;

    @PostMapping(value = "/articles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "아티클 저장")
    public CommonResponse registerArticle(@ModelAttribute ArticleReqDTO reqDTO) {
        log.info("requestDTO :{}", reqDTO.toString());
        log.info("imgOrgName :{}", reqDTO.getImg().isEmpty() ? null : reqDTO.getImg().getOriginalFilename());

        String userId = "TEST"; // todo 테스트 후 삭제해야함
        UserInfo userInfo = articleService.createUserInfo(userId); // todo 테스트 후 삭제해야함


        articleService.createArticle(userId, reqDTO);

        return responseService.getSuccessResponse();
    }

    @GetMapping("/articles")
    @ApiOperation(value = "아티클 불러오기")
    @ApiImplicitParam(name = "page", value = "페이지 번호(0...N)", required = true, dataType = "int", paramType = "query", example = "0")
    public CommonResponse getArticles(@RequestParam(defaultValue = "0") int page) {
        log.info("article search page: {}", page);

        PageRequest pageRequest = PageRequest.of(page, 10);
        pageRequest.withSort(Sort.Direction.ASC, "id");

        String userId = "TEST"; // todo 테스트 후 삭제해야함

        ArticleResDTO articleList = articleService.findAllArticlesByUserId(userId, pageRequest);

        log.info("article list: {}", articleList.toString());
        return responseService.getSingleResponse(articleList);
    }

    @DeleteMapping("/articles/{id}")
    @ApiOperation(value = "아티클 삭제")
    @ApiImplicitParam(name = "id", value = "아티클 아이디", required = true, dataType = "long", paramType = "path", example = "1")
    public CommonResponse deleteArticle(@PathVariable Long id) {
        log.info("deleteArticle id :{}", id);

        articleService.deleteArticle(id);

        return responseService.getSuccessResponse();
    }


}
