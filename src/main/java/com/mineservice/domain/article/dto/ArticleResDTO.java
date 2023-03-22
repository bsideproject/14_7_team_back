package com.mineservice.domain.article.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArticleResDTO {

    private Long totalArticleSize;

    private int totalPageSize;

    List<ArticleDTO> articleList;

}
