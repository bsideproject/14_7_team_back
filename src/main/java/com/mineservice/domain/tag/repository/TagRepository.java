package com.mineservice.domain.tag.repository;

import com.mineservice.domain.tag.domain.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    Optional<TagEntity> findByName(String tagName);

    Optional<TagEntity> findByUserIdAndName(String userId, String tagName);

    List<TagEntity> findAllByUserId(String userId);

    @Query(value = "SELECT t.* " +
            "FROM tag t " +
            "LEFT JOIN article_tag at ON at.tag_id = t.id " +
            "LEFT JOIN article a ON at.article_id = a.id " +
            "WHERE a.id = :articleId ", nativeQuery = true)
    List<TagEntity> findAllByArticleId(@Param("articleId") Long articleId);
}
