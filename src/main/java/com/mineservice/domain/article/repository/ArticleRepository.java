package com.mineservice.domain.article.repository;

import com.mineservice.domain.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByUserId(String userId, Pageable pageable);
}
