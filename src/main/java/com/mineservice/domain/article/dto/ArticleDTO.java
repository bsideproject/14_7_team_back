package com.mineservice.domain.article.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ArticleDTO {

    private Long articleId;
    private String title;
    private String type;
    private String favorite;

}
