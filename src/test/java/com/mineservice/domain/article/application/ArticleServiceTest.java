package com.mineservice.domain.article.application;

import com.mineservice.domain.article.dto.ArticleReqDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Test
    void createArticleTest () {
        String userId = "TEST_USER";

        ArticleReqDTO articleReqDTO = new ArticleReqDTO();
        articleReqDTO.setTitle("TEST_TITLE");
        articleReqDTO.setUrl("TEST_URL");
        articleReqDTO.setTags(List.of(new String[]{"TEST_TAG1", "TEST_TAG2"}));
        articleReqDTO.setAlarmTime(LocalDateTime.now());

        articleService.createArticle(userId, articleReqDTO);


    }
}