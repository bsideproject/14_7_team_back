package com.mineservice.domain.article.repository;

import com.mineservice.domain.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByUserId(String userId, Pageable pageable);

    List<Article> findArticlesByTitleStartingWith(String title);

    Optional<Article> findArticleByUrl(String url);
}
