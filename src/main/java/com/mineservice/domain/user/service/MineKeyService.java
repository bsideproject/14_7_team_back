package com.mineservice.domain.user.service;

import com.mineservice.domain.user.domain.MineKeyEntity;
import com.mineservice.domain.user.repository.MineKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MineKeyService {

    private final MineKeyRepository mineKeyRepository;

    public Optional<MineKeyEntity> findByUserId(String userId) {
        return mineKeyRepository.findByUserId(userId);
    }

    public Optional<MineKeyEntity> findByToken(String token) {
        return mineKeyRepository.findByToken(token);
    }

    public void createKey(String userId, String token) {
        mineKeyRepository.save(MineKeyEntity.builder()
                .userId(userId)
                .token(token)
                .build());
    }

    public void updateKey(MineKeyEntity mineKey, String userId, String token) {

        mineKeyRepository.save(MineKeyEntity.builder()
                .id(mineKey.getId())
                .userId(userId)
                .token(token)
                .modifyBy(userId)
                .modifyDt(LocalDateTime.now())
                .build());

    }

}
