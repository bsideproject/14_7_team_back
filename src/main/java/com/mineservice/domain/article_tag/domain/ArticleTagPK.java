package com.mineservice.domain.article_tag.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTagPK implements Serializable {

    private Long article;

    private Long tag;

}
