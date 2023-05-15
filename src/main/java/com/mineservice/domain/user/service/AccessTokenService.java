package com.mineservice.domain.user.service;

import com.mineservice.domain.user.domain.AccessTokenEntity;
import com.mineservice.domain.user.vo.UserInfo;

public interface AccessTokenService {
    AccessTokenEntity updateAccessTokenByMemberLogin(String userId, UserInfo userInfo);
}
