package com.mineservice.domain.article.api;

import com.mineservice.domain.article.application.ArticleService;
import com.mineservice.domain.article.dto.ArticleReqDTO;
import com.mineservice.domain.article.dto.ArticleResDTO;
import com.mineservice.domain.tag.application.TagService;
import com.mineservice.domain.user.domain.UserInfoEntity;
import com.mineservice.global.common.response.CommonResponse;
import com.mineservice.global.common.response.ResponseService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.headers.Header;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final TagService tagService;
    private final ResponseService responseService;

    @PostMapping(value = "/articles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "아티클 저장", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse registerArticle(@ModelAttribute ArticleReqDTO reqDTO,
                                          @ApiIgnore @AuthenticationPrincipal UserDetails user) {
        log.info("requestDTO :{}", reqDTO.toString());
        log.info("imgOrgName :{}", reqDTO.getImg().isEmpty() ? null : reqDTO.getImg().getOriginalFilename());

        String userId = user.getUsername();

        articleService.createArticle(userId, reqDTO);

        return responseService.getSuccessResponse();
    }

    @GetMapping("/articles")
    @ApiOperation(value = "아티클 불러오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호(0...N)", required = true, dataType = "int", paramType = "query", example = "0", defaultValue = "0"),
            @ApiImplicitParam(name = "favorite", value = "즐겨찾기 유무", dataType = "boolean", paramType = "query", example = ""),
            @ApiImplicitParam(name = "unread", value = "미열람 유무", dataType = "boolean", paramType = "query", example = ""),
            @ApiImplicitParam(name = "types", value = "콘텐츠타입(article, image, youtube)", dataType = "list", paramType = "query", example = ""),
            @ApiImplicitParam(name = "tags", value = "태그리스트(태그1,태그2,태그3)", dataType = "list", paramType = "query", example = ""),
            @ApiImplicitParam(name = "sort", value = "정렬(desc,asc)", required = true, dataType = "string", paramType = "query", example = "desc", defaultValue = "desc")
    })
    public CommonResponse getArticles(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(required = false) boolean favorite,
                                      @RequestParam(required = false) boolean unread,
                                      @RequestParam(required = false) List<String> types,
                                      @RequestParam(required = false) List<String> tags,
                                      @RequestParam(defaultValue = "desc") String sort,
                                      @ApiIgnore @AuthenticationPrincipal UserDetails user) {
        log.info("article search page: {}", page);

        String userId = user.getUsername();

        PageRequest pageRequest = PageRequest.of(page, 10);
        pageRequest.withSort(Sort.Direction.ASC, "id");

        ArticleResDTO articleList = articleService.findAllArticlesByUserId(userId, pageRequest);

        log.info("article list: {}", articleList.toString());
        return responseService.getSingleResponse(articleList);
    }

    @DeleteMapping("/articles/{id}")
    @ApiOperation(value = "아티클 삭제")
    @ApiImplicitParam(name = "id", value = "아티클 아이디", required = true, dataType = "long", paramType = "path", example = "1")
    public CommonResponse deleteArticle(@PathVariable Long id,
                                        @ApiIgnore @AuthenticationPrincipal UserDetails user) {
        log.info("deleteArticle id :{}", id);

        articleService.deleteArticle(id);

        String userId = user.getUsername();

        tagService.deleteTagByUserId(userId);

        return responseService.getSuccessResponse();
    }


}
