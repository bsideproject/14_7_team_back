package com.mineservice.domain.user.repository;

import com.mineservice.domain.user.domain.UserAlarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAlarmRepository extends JpaRepository<UserAlarmEntity, String> {
}
