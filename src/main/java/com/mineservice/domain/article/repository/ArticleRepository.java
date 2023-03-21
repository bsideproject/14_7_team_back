package com.mineservice.domain.article.repository;

import com.mineservice.domain.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
