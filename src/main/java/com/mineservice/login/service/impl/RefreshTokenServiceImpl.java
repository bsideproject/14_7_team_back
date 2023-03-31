package com.mineservice.login.service.impl;

import com.mineservice.domain.user.RefreshTokenRepository;
import com.mineservice.login.entity.RefreshToken;
import com.mineservice.login.service.RefreshTokenService;
import com.mineservice.login.vo.NaverUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public RefreshToken saveRefreshToken(String userId, NaverUserInfo userInfo) {
        RefreshToken refreshToken = RefreshToken.builder()
            .id(userId)
            .refreshToken(userInfo.getRefreshToken())
            .expireDt(userInfo.getAccessTokenExpireDate())
            .createBy(userId)
            .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken updateRefreshTokenByMemberLogin(String userId, NaverUserInfo userInfo) {
        RefreshToken refreshToken = refreshTokenRepository.findById(userId).get();
        refreshToken.setRefreshToken(userInfo.getRefreshToken());
        refreshToken.setExpireDt(userInfo.getAccessTokenExpireDate());
        refreshToken.setModifyBy(userId);
        return refreshTokenRepository.save(refreshToken);
    }
}
