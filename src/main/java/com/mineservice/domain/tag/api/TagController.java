package com.mineservice.domain.tag.api;

import com.mineservice.domain.tag.application.TagService;
import com.mineservice.domain.tag.dto.TagResDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/tags")
    @ApiOperation(value = "태그 목록 불러오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "아티클 id", dataType = "java.lang.Long", paramType = "query")
    })
    public ResponseEntity<TagResDTO> getTags(@RequestParam(required = false) Long articleId,
                                             @ApiIgnore @AuthenticationPrincipal UserDetails user,
                                             HttpServletRequest request) {
        log.info("api : {}", request.getRequestURI());
        String userId = user.getUsername();
        log.info("userId : {}", userId);
        TagResDTO resDTO = tagService.findAllTagName(articleId, userId);
        log.info("resDTO : {}", resDTO);
        return ResponseEntity.ok(resDTO);
    }


}
