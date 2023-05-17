package com.mineservice.domain.user.repository;

import com.mineservice.domain.user.domain.MineKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MineKeyRepository extends JpaRepository<MineKeyEntity, Integer> {

    Optional<MineKeyEntity> findByUserId(String userId);

    Optional<MineKeyEntity> findByToken(String token);

}
