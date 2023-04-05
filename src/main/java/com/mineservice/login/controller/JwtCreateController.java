package com.mineservice.login.controller;

import com.mineservice.domain.user.domain.UserInfoEntity;
import com.mineservice.login.JwtTokenProvider;
import com.mineservice.login.service.AccessTokenService;
import com.mineservice.login.service.RefreshTokenService;
import com.mineservice.login.service.UserInfoService;
import com.mineservice.login.vo.NaverUserInfo;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtCreateController {

    private final UserInfoService userInfoService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login/naver")
    public String jwtCreate(@RequestBody NaverUserInfo userInfo){

        UserInfoEntity userEntity = userInfoService.getUser(userInfo.getId(), userInfo.getProvider());


        String userId;
        List<String> roles;
        if(userEntity == null){ //최초 로그인 (회원가입)
            userId = "user_" + UUID.randomUUID().toString();
            LocalDateTime nowTime = LocalDateTime.now();

            roles = Collections.singletonList("ROLE_USER");

            userInfoService.joinUser(userId, userInfo);
        }else{//이미 회원일 경우
            userId = userEntity.getId();
            roles = userEntity.getRoles();

            userInfoService.memberLogin(userEntity);
            accessTokenService.updateAccessTokenByMemberLogin(userId, userInfo);
            refreshTokenService.updateRefreshTokenByMemberLogin(userId, userInfo);
        }


        return jwtTokenProvider.generateJwt(userId, roles);
    }

}
