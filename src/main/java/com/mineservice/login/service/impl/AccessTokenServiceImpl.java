package com.mineservice.login.service.impl;

import com.mineservice.domain.user.AccessTokenRepository;
import com.mineservice.login.entity.AccessToken;
import com.mineservice.login.service.AccessTokenService;
import com.mineservice.login.vo.NaverUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;

    @Override
    public AccessToken saveAccessToken(String userId, NaverUserInfo userInfo) {
        AccessToken accessToken = AccessToken.builder()
            .id(userId)
            .token(userInfo.getAccessToken())
            .expireDt(userInfo.getAccessTokenExpireDate())
            .createBy(userId)
            .build();

        return accessTokenRepository.save(accessToken);
    }

    @Override
    public AccessToken updateAccessTokenByMemberLogin(String userId, NaverUserInfo userInfo) {
        AccessToken accessToken = accessTokenRepository.findById(userId).get();
        accessToken.setToken(userInfo.getAccessToken());
        accessToken.setExpireDt(userInfo.getAccessTokenExpireDate());
        accessToken.setModifyBy(userId);
        return accessTokenRepository.save(accessToken);
    }
}
