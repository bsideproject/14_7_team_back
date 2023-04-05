package com.mineservice.login.service;

import com.mineservice.domain.user.domain.RefreshTokenEntity;
import com.mineservice.login.vo.NaverUserInfo;

public interface RefreshTokenService {

    RefreshTokenEntity updateRefreshTokenByMemberLogin(String userId, NaverUserInfo userInfo);
}
