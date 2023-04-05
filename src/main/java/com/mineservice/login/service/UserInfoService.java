package com.mineservice.login.service;

import com.mineservice.domain.user.domain.UserInfoEntity;
import com.mineservice.login.vo.NaverUserInfo;

public interface UserInfoService {

    UserInfoEntity getUser(String id, String provider);

    UserInfoEntity joinUser(String userId, NaverUserInfo userInfo);

    UserInfoEntity memberLogin(UserInfoEntity userEntity);
}
