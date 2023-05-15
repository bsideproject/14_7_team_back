package com.mineservice.domain.user.service.impl;

import com.mineservice.domain.user.domain.AccessTokenEntity;
import com.mineservice.domain.user.repository.AccessTokenRepository;
import com.mineservice.domain.user.service.AccessTokenService;
import com.mineservice.domain.user.vo.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;

    @Override
    public AccessTokenEntity updateAccessTokenByMemberLogin(String userId, UserInfo userInfo) {
        AccessTokenEntity accessToken = accessTokenRepository.findById(userId).get();
        accessToken.setToken(userInfo.getAccessToken());
        accessToken.setExpireDt(userInfo.getAccessTokenExpireDate());
        accessToken.setModifyBy(userId);
        return accessTokenRepository.save(accessToken);
    }
}