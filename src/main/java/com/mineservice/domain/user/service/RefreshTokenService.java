package com.mineservice.domain.user.service;

import com.mineservice.domain.user.domain.RefreshTokenEntity;
import com.mineservice.domain.user.vo.UserInfo;

public interface RefreshTokenService {

    RefreshTokenEntity updateRefreshTokenByMemberLogin(String userId, UserInfo userInfo);
}
