package com.mineservice.domain.article.repository;

import com.mineservice.domain.article.domain.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    Page<ArticleEntity> findAllByUserId(String userId, Pageable pageable);

    List<ArticleEntity> findArticlesByUserIdAndTitleStartingWith(String userId, String title);

    Optional<ArticleEntity> findArticleByUrl(String url);
}
