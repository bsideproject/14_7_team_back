package com.mineservice.domain.tag.repository;

import com.mineservice.domain.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String tagName);

    Optional<Tag> findByUserIdAndName(String userId, String tagName);

    List<Tag> findAllByUserId(String userId);
}
