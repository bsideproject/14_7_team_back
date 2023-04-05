package com.mineservice.domain.user.repository;

import com.mineservice.domain.user.domain.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {

    UserInfoEntity findByUidAndProvider(String id, String provider);
}
