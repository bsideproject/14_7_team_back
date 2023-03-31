package com.mineservice.login.service;

import com.mineservice.login.entity.RefreshToken;
import com.mineservice.login.vo.NaverUserInfo;

public interface RefreshTokenService {

    RefreshToken saveRefreshToken(String userId, NaverUserInfo userInfo);

    RefreshToken updateRefreshTokenByMemberLogin(String userId, NaverUserInfo userInfo);
}
