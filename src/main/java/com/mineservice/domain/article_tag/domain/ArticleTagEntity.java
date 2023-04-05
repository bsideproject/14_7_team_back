package com.mineservice.domain.article_tag.domain;

import com.mineservice.domain.article.domain.ArticleEntity;
import com.mineservice.domain.tag.domain.TagEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@IdClass(ArticleTagPK.class)
@NoArgsConstructor
@Table(name = "article_tag")
public class ArticleTagEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private TagEntity tag;

    @Builder
    public ArticleTagEntity(ArticleEntity article, TagEntity tag) {
        this.article = article;
        this.tag = tag;
    }
}
