package com.mineservice.domain.article.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ArticleResDTO {

    private Long totalArticleSize;

    private int totalPageSize;

    List<ArticleDTO> articleList;

}
