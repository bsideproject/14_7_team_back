package com.mineservice.domain.push.repository;

import com.mineservice.domain.push.domain.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, String> {

    Optional<DeviceToken> findByUserId(String userId);

    void deleteByUserId(String userId);
}
