package com.mineservice.domain.article.repository;

import com.mineservice.domain.article.domain.ArticleAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleAlarmRepository extends JpaRepository<ArticleAlarm, Long> {
    void deleteByArticleId(Long articleId);

    Optional<ArticleAlarm> findOneByArticleId(Long articleId);
}
