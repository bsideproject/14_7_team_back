package com.mineservice.domain.user.controller;

import com.mineservice.domain.user.JwtTokenProvider;
import com.mineservice.domain.user.domain.UserInfoEntity;
import com.mineservice.domain.user.service.AccessTokenService;
import com.mineservice.domain.user.service.RefreshTokenService;
import com.mineservice.domain.user.service.UserInfoService;
import com.mineservice.domain.user.vo.UserInfo;
import com.mineservice.domain.user.vo.response.ResponseJwt;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NaverLoginController {

    private final UserInfoService userInfoService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login/naver")
    @ApiOperation(value = "네이버 로그인")
    public ResponseEntity<ResponseJwt> jwtCreate(@RequestBody UserInfo userInfo,
                                                 HttpServletRequest request) {
        log.info("api : {}", request.getRequestURI());
        log.info("request : {}", userInfo.toString());

        UserInfoEntity userEntity = userInfoService.findByIdAndProvider(userInfo.getId(), userInfo.getProvider());

        String userId;
        List<String> roles;
        String type = "";
        if (userEntity == null) { //최초 로그인 (회원가입)
            userId = "user_" + UUID.randomUUID().toString();
            log.info("회원가입 userId : {}", userId);

            roles = Collections.singletonList("ROLE_USER");

            userInfoService.joinUser(userId, userInfo);
            type = "join";
        } else {//이미 회원일 경우
            log.info("findByIdAndProvider : {}", userEntity.toString());
            userId = userEntity.getId();
            roles = userEntity.getRoles();
            log.info("기존회원 userId : {}", userId);

            userInfoService.memberLogin(userEntity);
            accessTokenService.updateAccessTokenByMemberLogin(userId, userInfo);
            refreshTokenService.updateRefreshTokenByMemberLogin(userId, userInfo);
            type = "re-login";
        }

        String jwt = jwtTokenProvider.generateJwt(userId, roles);
        log.info("created JWT Token : {}", jwt);
        ResponseJwt responseJwt = new ResponseJwt();
        responseJwt.setAccessToken(jwt);
        responseJwt.setType(type);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseJwt);
    }

}
