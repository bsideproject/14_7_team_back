package com.mineservice.domain.article.repository;

import com.mineservice.domain.article.domain.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    List<ArticleEntity> findArticlesByUserIdAndTitleStartingWith(String userId, String title);

    Optional<ArticleEntity> findArticleByUrlAndUserId(String url, String userId);

    Optional<ArticleEntity> findArticleByIdAndUserId(Long id, String userId);

    @Query(value = "SELECT a.* " +
            "FROM article a " +
            "LEFT JOIN article_tag atag ON a.id = atag.article_id " +
            "LEFT JOIN tag t ON t.id = atag.tag_id " +
            "WHERE a.user_id = :userId " +
            "AND (:title is null OR a.title LIKE %:title%) " +
            "AND (:favorite is null OR a.favorite = :favorite) " +
            "AND (:readYn is null OR a.read_yn = :readYn) " +
            "AND (:types is null OR a.type IN (:types)) " +
            "AND (:tags is null OR t.name IN (:tags)) " +
            "GROUP BY a.id ", nativeQuery = true,
            countQuery = "SELECT COUNT(distinct a.id) " +
                    "FROM article a " +
                    "LEFT JOIN article_tag atag ON a.id = atag.article_id " +
                    "LEFT JOIN tag t ON t.id = atag.tag_id " +
                    "WHERE a.user_id = :userId " +
                    "AND (:title is null OR a.title LIKE %:title%) " +
                    "AND (:favorite is null OR a.favorite = :favorite) " +
                    "AND (:readYn is null OR a.read_yn = :readYn) " +
                    "AND (:types is null OR a.type IN (:types)) " +
                    "AND (:tags is null OR t.name IN (:tags)) " +
                    "GROUP BY a.user_id ")
    Page<ArticleEntity> findAllBySearch(@Param("title") String title,
                                        @Param("favorite") String favorite,
                                        @Param("readYn") String readYn,
                                        @Param("types") List<String> types,
                                        @Param("tags") List<String> tags,
                                        @Param("userId") String userId,
                                        Pageable pageable);
}
