package com.mineservice.domain.article.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArticleResDTO {

    private Long articleId;
    private String title;
    private String type;


}
