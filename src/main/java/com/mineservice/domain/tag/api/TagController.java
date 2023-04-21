package com.mineservice.domain.tag.api;

import com.mineservice.domain.tag.application.TagService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;


    @ApiOperation(value = "태그 목록 불러오기")
    @GetMapping("/tags")
    public ResponseEntity<List<String>> getTags(@ApiIgnore @AuthenticationPrincipal UserDetails user) {
        String userId = user.getUsername();
        log.info("userId : {}", userId);

        List<String> allTagNameByUserId = tagService.findAllTagNameByUserId(userId);
        log.info("tag list: {}", allTagNameByUserId);

        return ResponseEntity.ok(allTagNameByUserId);
    }


}
