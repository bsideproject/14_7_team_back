package com.mineservice.domain.user.repository;

import com.mineservice.domain.user.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
}
