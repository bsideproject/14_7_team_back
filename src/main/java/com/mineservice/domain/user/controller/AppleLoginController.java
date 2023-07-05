package com.mineservice.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mineservice.domain.user.JwtTokenProvider;
import com.mineservice.domain.user.domain.UserInfoEntity;
import com.mineservice.domain.user.service.AccessTokenService;
import com.mineservice.domain.user.service.RefreshTokenService;
import com.mineservice.domain.user.service.UserInfoService;
import com.mineservice.domain.user.vo.UserInfo;
import com.mineservice.domain.user.vo.response.ResponseJwt;
import com.mineservice.global.AppleLoginUtil;
import com.mineservice.global.exception.CustomException;
import com.mineservice.global.exception.ErrorCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AppleLoginController {

    private static final String TEAM_ID = "XQ24HP64B3";
    private static final String REDIRECT_URL = "https://mine.directory/login/oauth_apple";
    private static final String CLIENT_ID = "com.bside.Mine.services";
    private static final String BUNDLE_ID = "com.bside.Mine";
    private static final String KEY_ID = "5UUZ4LKXZF";
    private static final String AUTH_URL = "https://appleid.apple.com";
    private static final String KEY_PATH = "static/apple/AuthKey_5UUZ4LKXZF.p8";

    private final AppleLoginUtil appleLoginUtil;
    private final UserInfoService userInfoService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login/oauth_apple")
    @ApiOperation(value = "애플 로그인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "code", dataType = "java.lang.String", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "사용자 이름", dataType = "java.lang.String", paramType = "query")
    })
    @ApiResponses(
            @ApiResponse(responseCode = "404", description = "유저 정보가 없을 경우")
    )
    public ResponseEntity<ResponseJwt> oauthApple(@RequestParam(value = "code") String code,
                                                  @RequestParam(value = "userName", required = false) String userName,
                                                  HttpServletRequest request) throws JsonProcessingException {
        log.info("api : {}", request.getRequestURI());
        log.info("code : {}", code);
        log.info("userName : {}", userName);

        String clientSecret = appleLoginUtil.createClientSecret(TEAM_ID, BUNDLE_ID, KEY_ID, KEY_PATH, AUTH_URL);

        String reqUrl = AUTH_URL + "/auth/token";
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("client_id", BUNDLE_ID);
        tokenRequest.put("client_secret", clientSecret);
        tokenRequest.put("code", code);
        tokenRequest.put("grant_type", "authorization_code");

        String apiResponse = appleLoginUtil.doPost(reqUrl, tokenRequest);

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject tokenResponse = objectMapper.readValue(apiResponse, JSONObject.class);
        log.info("tokenResponse : {}", tokenResponse.toString());

        if (tokenResponse.get("error") == null) {
            JSONObject payload = appleLoginUtil.decodeFromIdToken(tokenResponse.getAsString("id_token"));

            log.info("payload : {}", payload.toString());

            String appleUniqueNo = payload.getAsString("sub");
            String jwt = userJoin(appleUniqueNo, userName, tokenResponse, payload);
            log.info("created JWT Token : {}", jwt);
            ResponseJwt responseJwt = new ResponseJwt();
            responseJwt.setAccessToken(jwt);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseJwt);
        } else {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    private String userJoin(String appleUniqueNo, String userName, JSONObject tokenResponse, JSONObject payload) {
        UserInfoEntity userEntity = userInfoService.findByIdAndProvider(appleUniqueNo, "apple");

        String userId;
        List<String> roles;
        if (userEntity == null) { //최초 로그인 (회원가입)
            userId = "user_" + UUID.randomUUID().toString();
            log.info("회원가입 userId : {}", userId);

            roles = Collections.singletonList("ROLE_USER");

            long exp = Long.parseLong(payload.getAsString("exp")) * 1000;
            Instant instant = Instant.ofEpochMilli(exp);
            LocalDateTime expirationDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));

            UserInfo userInfo = new UserInfo();
            userInfo.setId(appleUniqueNo);
            userInfo.setAccessToken(tokenResponse.getAsString("access_token"));
            userInfo.setRefreshToken(tokenResponse.getAsString("refresh_token"));
            userInfo.setEmail(payload.getAsString("email"));
            userInfo.setAccessTokenExpireDate(expirationDateTime);
            userInfo.setProvider("apple");
            if (userName == null || " ".equals(userName) || "".equals(userName.trim())) {
                userInfo.setName("유저");
            } else {
                userInfo.setName(userName);
            }

            userInfoService.joinUser(userId, userInfo);
        } else {//이미 회원일 경우
            log.info("findByIdAndProvider : {}", userEntity.toString());
            userId = userEntity.getId();
            roles = userEntity.getRoles();
            log.info("기존회원 userId : {}", userId);

            userInfoService.memberLogin(userEntity);

            UserInfo userInfo = new UserInfo();
            userInfo.setAccessToken(tokenResponse.getAsString("access_token"));
            userInfo.setRefreshToken(tokenResponse.getAsString("refresh_token"));

            accessTokenService.updateAccessTokenByMemberLogin(userId, userInfo);
            refreshTokenService.updateRefreshTokenByMemberLogin(userId, userInfo);
        }

        return jwtTokenProvider.generateJwt(userId, roles);
    }
}
