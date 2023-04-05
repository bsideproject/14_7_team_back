package com.mineservice.domain.article_tag.repository;

import com.mineservice.domain.article_tag.domain.ArticleTagEntity;
import com.mineservice.domain.article_tag.domain.ArticleTagPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleTagRepository extends JpaRepository<ArticleTagEntity, ArticleTagPK> {
    void deleteByArticleId(Long articleId);

    Optional<ArticleTagEntity> findByTagId(Long tagId);
}
