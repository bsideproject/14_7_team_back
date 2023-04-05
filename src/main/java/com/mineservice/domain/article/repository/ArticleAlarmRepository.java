package com.mineservice.domain.article.repository;

import com.mineservice.domain.article.domain.ArticleAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleAlarmRepository extends JpaRepository<ArticleAlarm, Long> {
    void deleteByArticleId(Long articleId);
}
