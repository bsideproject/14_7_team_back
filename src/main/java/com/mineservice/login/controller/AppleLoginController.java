package com.mineservice.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mineservice.domain.user.domain.UserInfoEntity;
import com.mineservice.global.AppleLoginUtil;
import com.mineservice.login.service.UserInfoService;
import com.mineservice.login.vo.UserInfo;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AppleLoginController {

    private static final String TEAM_ID = "XQ24HP64B3";
    private static final String REDIRECT_URL = "https://mine.directory/login/oauth_apple";
    private static final String CLIENT_ID = "com.bside.Mine.services";
    private static final String KEY_ID = "5UUZ4LKXZF";
    private static final String AUTH_URL = "https://appleid.apple.com";
    private static final String KEY_PATH = "static/apple/AuthKey_5UUZ4LKXZF.p8";

    private final AppleLoginUtil appleLoginUtil;
    private final UserInfoService userInfoService;

    @GetMapping("/login/getAppleAuthUrl")
    public @ResponseBody String getAppleAuthUrl(HttpServletRequest request) {
        String reqUrl = AUTH_URL + "/auth/authorize?client_id="
            + CLIENT_ID
            + "&redirect_uri="
            + REDIRECT_URL
            + "&response_type=code id_token&scope=name email&response_mode=form_post";
        return reqUrl;
    }

    @PostMapping("/login/oauth_apple")
    public @ResponseBody String oauthApple(HttpServletRequest request,
        @RequestParam(value = "code", required = false) String code, @RequestParam(value = "user", required = false) String user,
        HttpServletResponse response,
        Model model) throws NoSuchAlgorithmException, JsonProcessingException, ParseException {
        if(code == null){
            return "index.html";
        }

        if(user != null){
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(user);
            org.json.simple.JSONObject jsonObj = (org.json.simple.JSONObject) obj;
            Object name = jsonObj.get("name");
            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) name;
            String s = (String) jsonObject.get("firstName") + (String) jsonObject.get("lastName");

            log.info((String) jsonObj.get("email"));
        }

        String clientId = CLIENT_ID;
        String clientSecret = appleLoginUtil.createClientSecret(TEAM_ID, CLIENT_ID, KEY_ID, KEY_PATH, AUTH_URL);

        String reqUrl = AUTH_URL + "/auth/token";
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("client_id", clientId);
        tokenRequest.put("client_secret", clientSecret);
        tokenRequest.put("code", code);
        tokenRequest.put("grant_type", "authorization_code");

        String apiResponse = appleLoginUtil.doPost(reqUrl, tokenRequest);

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject tokenResponse = objectMapper.readValue(apiResponse, JSONObject.class);

        if(tokenResponse.get("error") == null){
            JSONObject payload = appleLoginUtil.decodeFromIdToken(tokenResponse.getAsString("id_token"));
            String appleUniqueNo = payload.getAsString("sub");
            userJoin(appleUniqueNo, tokenResponse, payload);
            return appleUniqueNo;

        }else{
            throw new UsernameNotFoundException("사용자 정보가 없습니다.");
        }
    }

    private void userJoin(String appleUniqueNo, JSONObject tokenResponse, JSONObject payload) {
        UserInfoEntity userEntity = userInfoService.getUser(appleUniqueNo, "apple");

        String userId;
        List<String> roles;
        if(userEntity == null){ //최초 로그인 (회원가입)
            userId = "user_" + UUID.randomUUID().toString();
            LocalDateTime nowTime = LocalDateTime.now();

            roles = Collections.singletonList("ROLE_USER");

            long exp = Long.parseLong(payload.getAsString("exp")) * 1000;
            Instant instant = Instant.ofEpochMilli(exp);
            LocalDateTime expirationDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));


            UserInfo userInfo = new UserInfo();
            userInfo.setAccessToken(tokenResponse.getAsString("access_token"));
            userInfo.setRefreshToken(tokenResponse.getAsString("refresh_token"));
            userInfo.setEmail(payload.getAsString("email"));
            userInfo.setAccessTokenExpireDate(expirationDateTime);
            userInfo.setProvider("apple");
//            userInfo

//            userInfoService.joinUser(userId, userInfo);
        }else{//이미 회원일 경우
            userId = userEntity.getId();
            roles = userEntity.getRoles();

            userInfoService.memberLogin(userEntity);
//            accessTokenService.updateAccessTokenByMemberLogin(userId, userInfo);
//            refreshTokenService.updateRefreshTokenByMemberLogin(userId, userInfo);
        }
    }
}
