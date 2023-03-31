package com.mineservice.login.service;

import com.mineservice.login.entity.AccessToken;
import com.mineservice.login.vo.NaverUserInfo;

public interface AccessTokenService {

    AccessToken saveAccessToken(String userId, NaverUserInfo userInfo);

    AccessToken updateAccessTokenByMemberLogin(String userId, NaverUserInfo userInfo);
}
