package com.mineservice.domain.article.api;

import com.mineservice.domain.article.application.ArticleService;
import com.mineservice.domain.article.dto.ArticleReqDTO;
import com.mineservice.domain.article.dto.ArticleResDTO;
import com.mineservice.domain.tag.application.TagService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping(value = "/articles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "아티클 저장", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerArticle(@ModelAttribute ArticleReqDTO reqDTO,
                                                  @ApiIgnore @AuthenticationPrincipal UserDetails user) {
        log.info("requestDTO :{}", reqDTO.toString());
        log.info("imgOrgName :{}", reqDTO.getImg() == null ? null : reqDTO.getImg().getOriginalFilename());

        String userId = user.getUsername();
        log.info("userId :{}", userId);

        articleService.createArticle(userId, reqDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/articles")
    @ApiOperation(value = "아티클 불러오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호(0...N)", dataType = "java.lang.Integer", paramType = "query", example = "0", defaultValue = "0"),
            @ApiImplicitParam(name = "title", value = "제목", dataType = "java.lang.String", paramType = "query", example = ""),
            @ApiImplicitParam(name = "favorite", value = "즐겨찾기유무(즐찾:true|미즐찾:false)", dataType = "java.lang.Boolean", paramType = "query", example = ""),
            @ApiImplicitParam(name = "readYn", value = "열람여부(열람:true|미열람:false)", dataType = "java.lang.Boolean", paramType = "query", example = ""),
            @ApiImplicitParam(name = "types", value = "콘텐츠타입(article|image|youtube)", dataType = "java.lang.String", allowMultiple = true, paramType = "query", example = ""),
            @ApiImplicitParam(name = "tags", value = "태그리스트(태그1,태그2,태그3)", dataType = "java.lang.String", allowMultiple = true, paramType = "query", example = ""),
            @ApiImplicitParam(name = "sort", value = "정렬(오래된순:asc|최근순:desc)", dataType = "java.lang.String", paramType = "query", example = "desc", defaultValue = "desc")
    })
    public ResponseEntity<ArticleResDTO> getArticles(@RequestParam(defaultValue = "0", required = false) int page,
                                                     @RequestParam(required = false) String title,
                                                     @RequestParam(required = false) boolean favorite,
                                                     @RequestParam(required = false) boolean readYn,
                                                     @RequestParam(required = false) List<String> types,
                                                     @RequestParam(required = false) List<String> tags,
                                                     @RequestParam(defaultValue = "desc", required = false) String sort,
                                                     @ApiIgnore @AuthenticationPrincipal UserDetails user) {
        log.info("article search page: {}", page);

        String userId = user.getUsername();

        PageRequest pageRequest = PageRequest.of(page, 10).withSort("asc".equals(sort) ? Sort.Direction.ASC : Sort.Direction.DESC, "create_dt");

        ArticleResDTO articleList = articleService.findAllBySearch(title, favorite, readYn, types, tags, userId, pageRequest);

        log.info("article list: {}", articleList.toString());
        return ResponseEntity.ok(articleList);
    }

    @DeleteMapping("/articles/{id}")
    @ApiOperation(value = "아티클 삭제")
    @ApiImplicitParam(name = "id", value = "아티클 아이디", required = true, dataType = "java.lang.Long", paramType = "path", example = "1")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id,
                                                @ApiIgnore @AuthenticationPrincipal UserDetails user) {
        log.info("deleteArticle id :{}", id);

        articleService.deleteArticle(id);

        String userId = user.getUsername();

        tagService.deleteTagByUserId(userId);

        return ResponseEntity.ok().build();
    }


}
