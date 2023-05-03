package com.mineservice.login.service;

import com.mineservice.domain.user.domain.AccessTokenEntity;
import com.mineservice.login.vo.UserInfo;

public interface AccessTokenService {
    AccessTokenEntity updateAccessTokenByMemberLogin(String userId, UserInfo userInfo);
}
