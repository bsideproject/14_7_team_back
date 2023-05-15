package com.mineservice.domain.user.service;

import com.mineservice.domain.user.domain.UserInfoEntity;
import com.mineservice.domain.user.vo.UserInfo;

public interface UserInfoService {

    UserInfoEntity getUser(String id, String provider);

    UserInfoEntity joinUser(String userId, UserInfo userInfo);

    UserInfoEntity memberLogin(UserInfoEntity userEntity);
}
