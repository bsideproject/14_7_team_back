package com.mineservice.domain.user.service.impl;

import com.mineservice.domain.user.domain.AccessTokenEntity;
import com.mineservice.domain.user.domain.RefreshTokenEntity;
import com.mineservice.domain.user.domain.UserAlarmEntity;
import com.mineservice.domain.user.domain.UserInfoEntity;
import com.mineservice.domain.user.repository.AccessTokenRepository;
import com.mineservice.domain.user.repository.RefreshTokenRepository;
import com.mineservice.domain.user.repository.UserAlarmRepository;
import com.mineservice.domain.user.repository.UserInfoRepository;
import com.mineservice.domain.user.service.UserInfoService;
import com.mineservice.domain.user.vo.UserInfo;
import java.time.LocalDateTime;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserAlarmRepository userAlarmRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public UserInfoEntity getUser(String id, String provider) {
        return userInfoRepository.findByUidAndProvider(id, provider);
    }

    @Override
    public UserInfoEntity joinUser(String userId, UserInfo userInfo) {

        AccessTokenEntity accessToken = AccessTokenEntity.builder()
            .userId(userId)
            .token(userInfo.getAccessToken())
            .expireDt(userInfo.getAccessTokenExpireDate())
            .createBy(userId)
            .build();

        accessTokenRepository.save(accessToken);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
            .userId(userId)
            .token(userInfo.getRefreshToken())
            .expireDt(userInfo.getAccessTokenExpireDate())
            .createBy(userId)
            .build();

        refreshTokenRepository.save(refreshToken);

        UserAlarmEntity userAlarm = UserAlarmEntity.builder()
                .userId(userId)
                .notiYn("N")
                .createBy(userId)
                .createDt(LocalDateTime.now())
                .build();
        userAlarmRepository.save(userAlarm);

        UserInfoEntity entity = UserInfoEntity.builder()
            .id(userId)
            .userName(userInfo.getName())
            .userEmail(userInfo.getEmail())
            .uid(userInfo.getId())
            .provider(userInfo.getProvider())
            .lastLoginDt(LocalDateTime.now())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .createDt(LocalDateTime.now())
            .createBy(userId)
            .roles(Collections.singletonList("ROLE_USER"))
            .build();

        UserInfoEntity user = userInfoRepository.save(entity);
        return user;
    }

    @Override
    public UserInfoEntity memberLogin(UserInfoEntity entity) {
        entity.setLastLoginDt(LocalDateTime.now());
        entity.setModifyBy(entity.getId());
        return userInfoRepository.save(entity);
    }
}
