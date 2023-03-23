package com.mineservice.domain.file_info.repository;

import com.mineservice.domain.file_info.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
}
