package com.mineservice.domain.article.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ArticleDTO {

    private final Long articleId; // article_id
    private final String title; // title
    private final String type; // article_type
    private final boolean favorite; // 즐겨찾기 유무
    private final boolean read; // 열람여부
    private final boolean alarm; // 알람여부
    private final String thumbUrl; // 썸네일 URL
    private final String url; // 아티클 url
    private final List<String> tagNames; // 태그리스트

}
