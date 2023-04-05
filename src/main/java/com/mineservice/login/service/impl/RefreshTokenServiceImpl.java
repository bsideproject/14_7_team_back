package com.mineservice.login.service.impl;

import com.mineservice.domain.user.domain.RefreshTokenEntity;
import com.mineservice.domain.user.repository.RefreshTokenRepository;
import com.mineservice.login.service.RefreshTokenService;
import com.mineservice.login.vo.NaverUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshTokenEntity updateRefreshTokenByMemberLogin(String userId, NaverUserInfo userInfo) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findById(userId).get();
        refreshToken.setToken(userInfo.getRefreshToken());
        refreshToken.setExpireDt(userInfo.getAccessTokenExpireDate());
        refreshToken.setModifyBy(userId);
        return refreshTokenRepository.save(refreshToken);
    }
}
