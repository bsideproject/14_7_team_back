package com.mineservice.login.service.impl;

import com.mineservice.domain.user.UserRepository;
import com.mineservice.login.entity.User;
import com.mineservice.login.service.UserService;
import com.mineservice.login.vo.NaverUserInfo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public User getUser(String id, String provider) {
        return userRepository.findByUidAndProvider(id, provider);
    }

    @Override
    public User joinUser(String userId, NaverUserInfo userInfo) {

        User user = User.builder()
            .id(userId)
            .name(userInfo.getName())
            .email(userInfo.getEmail())
            .uid(userInfo.getId())
            .provider(userInfo.getProvider())
            .lastLoginDt(LocalDateTime.now())
            .createBy(userId)
            .build();
        return userRepository.save(user);
    }

    @Override
    public User memberLogin(User userEntity) {
        userEntity.setLastLoginDt(LocalDateTime.now());
        userEntity.setModifyBy(userEntity.getId());
        return userRepository.save(userEntity);
    }
}
