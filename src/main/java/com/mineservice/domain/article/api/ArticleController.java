package com.mineservice.domain.article.api;

import com.mineservice.domain.article.domain.Article;
import com.mineservice.domain.article.dto.ArticleReqDTO;
import com.mineservice.domain.article.repository.ArticleRepository;
import com.mineservice.domain.tag.domain.Tag;
import com.mineservice.domain.tag.repository.TagRepository;
import com.mineservice.global.common.response.CommonResponse;
import com.mineservice.global.common.response.ResponseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

    public static List<ArticleReqDTO> dummyList = new ArrayList<>();
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final ResponseService responseService;

    @PostMapping(value = "/articles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "아티클 저장")
    public CommonResponse registerArticle(@ModelAttribute ArticleReqDTO reqDTO) {
        log.info("requestDTO :{}", reqDTO.toString());
        log.info("imgOrgName :{}", reqDTO.getImg().isEmpty() ? null : reqDTO.getImg().getOriginalFilename());


        Article article = articleRepository.save(Article.builder()
                .title(reqDTO.getTitle())
                .type("") // todo : type
                .url(reqDTO.getUrl())
                .build());

        log.info("save article :{}", article.toString());

        List<Tag> tagList = tagRepository.saveAll(reqDTO.getTags().stream()
                .map(tag -> Tag.builder()
                        .userId("") // todo : userId
                        .name(tag)
                        .createBy("") // todo : createBy
                        .createDt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList()));

        log.info("save all tagList :{}", tagList.toString());

        dummyList.add(reqDTO);

        return responseService.getSuccessResponse();
    }

    @GetMapping("/articles")
    @ApiOperation(value = "아티클 불러오기")
    public CommonResponse getArticles() {
        log.info("responseBody :{}", dummyList.toString());

        return responseService.getListResponse(dummyList);
    }

    @DeleteMapping("/articles/{id}")
    @ApiOperation(value = "아티클 삭제")
    @ApiImplicitParam(name = "id", value = "아티클 아이디", required = true, dataType = "long", paramType = "path", example = "1")
    public CommonResponse deleteArticle(@PathVariable Long id) {
        log.info("deleteArticle id :{}", id);

        return responseService.getSuccessResponse();
    }


}
