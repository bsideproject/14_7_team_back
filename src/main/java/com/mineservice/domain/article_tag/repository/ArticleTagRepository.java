package com.mineservice.domain.article_tag.repository;

import com.mineservice.domain.article_tag.domain.ArticleTag;
import com.mineservice.domain.article_tag.domain.ArticleTagPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, ArticleTagPK> {
    void deleteByArticleId(Long articleId);
}
