package com.mineservice.domain.file_info.repository;

import com.mineservice.domain.file_info.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    Optional<FileInfo> findByArticleId(Long articleId);

    void deleteByArticleId(Long articleId);
}
