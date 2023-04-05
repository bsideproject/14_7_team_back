package com.mineservice.domain.tag.repository;

import com.mineservice.domain.tag.domain.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    Optional<TagEntity> findByName(String tagName);

    Optional<TagEntity> findByUserIdAndName(String userId, String tagName);

    List<TagEntity> findAllByUserId(String userId);
}
