package com.mineservice.login.service;

import com.mineservice.login.entity.User;
import com.mineservice.login.vo.NaverUserInfo;

public interface UserService {

    User getUser(String id, String provider);

    User joinUser(String userId, NaverUserInfo userInfo);

    User memberLogin(User userEntity);
}
