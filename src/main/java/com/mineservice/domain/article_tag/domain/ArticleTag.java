package com.mineservice.domain.article_tag.domain;

import com.mineservice.domain.article.domain.Article;
import com.mineservice.domain.tag.domain.Tag;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@IdClass(ArticleTagPK.class)
@NoArgsConstructor
public class ArticleTag {

    @Id
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    public ArticleTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }
}
