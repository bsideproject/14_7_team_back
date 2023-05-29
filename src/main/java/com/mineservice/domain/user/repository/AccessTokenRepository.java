package com.mineservice.domain.user.repository;

import com.mineservice.domain.user.domain.AccessTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessTokenEntity, String> {

    void deleteByUserId(String userId);
}
