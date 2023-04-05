package com.mineservice.domain.file_info.repository;

import com.mineservice.domain.file_info.domain.FileInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileInfoRepository extends JpaRepository<FileInfoEntity, Long> {
    Optional<FileInfoEntity> findByArticleId(Long articleId);

    void deleteByArticleId(Long articleId);
}
