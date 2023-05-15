package com.mineservice.domain.push.repository;

import com.mineservice.domain.push.domain.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, String> {

}
