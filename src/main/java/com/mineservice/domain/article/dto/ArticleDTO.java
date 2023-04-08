package com.mineservice.domain.article.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ArticleDTO {

    private Long articleId;
    private String title;
    private String type;
    private String favorite;
    private String read;
    private List<String> tagNames;

}
