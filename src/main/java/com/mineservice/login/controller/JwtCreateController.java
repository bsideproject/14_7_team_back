package com.mineservice.login.controller;

import com.mineservice.domain.user.AccessTokenRepository;
import com.mineservice.domain.user.RefreshTokenRepository;
import com.mineservice.domain.user.UserRepository;
import com.mineservice.login.entity.User;
import com.mineservice.login.service.AccessTokenService;
import com.mineservice.login.service.RefreshTokenService;
import com.mineservice.login.service.UserService;
import com.mineservice.login.vo.NaverUserInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtCreateController {

    private final UserRepository userRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/oauth/jwt/naver")
    public String jwtCreate(@RequestBody NaverUserInfo userInfo){

        User userEntity = userService.getUser(userInfo.getId(), userInfo.getProvider());

        String userId;
        if(userEntity == null){ //최초 로그인 (회원가입)
            userId = "user_" + UUID.randomUUID().toString();
            LocalDateTime nowTime = LocalDateTime.now();

            userService.joinUser(userId, userInfo);

            accessTokenService.saveAccessToken(userId, userInfo);

            refreshTokenService.saveRefreshToken(userId, userInfo);
        }else{//이미 회원일 경우
            userId = userEntity.getId();

            userService.memberLogin(userEntity);

            accessTokenService.updateAccessTokenByMemberLogin(userId, userInfo);

            refreshTokenService.updateRefreshTokenByMemberLogin(userId, userInfo);
        }


        String jwt = Jwts.builder()
            .setSubject(userId)
            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(SignatureAlgorithm.HS512, "user_token")
            .compact();

        return jwt;

    }

}
