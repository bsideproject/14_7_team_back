package com.mineservice.login.service;

import com.mineservice.domain.user.domain.AccessTokenEntity;
import com.mineservice.login.vo.NaverUserInfo;

public interface AccessTokenService {
    AccessTokenEntity updateAccessTokenByMemberLogin(String userId, NaverUserInfo userInfo);
}
