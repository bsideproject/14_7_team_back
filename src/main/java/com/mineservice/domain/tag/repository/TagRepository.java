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

    @Query(value = "SELECT t.* " +
            "FROM tag t " +
            "LEFT JOIN article_tag artag ON artag.tag_id = t.id " +
            "WHERE t.user_id = :userId " +
            "ORDER BY artag.tag_id DESC", nativeQuery = true)
    List<TagEntity> findAllByUserId(@Param("userId") String userId);

    @Query(value = "SELECT t.* " +
            "FROM tag t " +
            "LEFT JOIN article_tag artag ON artag.tag_id = t.id " +
            "LEFT JOIN article a ON artag.article_id = a.id " +
            "WHERE a.id = :articleId " +
            "ORDER BY artag.tag_id DESC", nativeQuery = true)
    List<TagEntity> findAllByArticleId(@Param("articleId") Long articleId);
}
