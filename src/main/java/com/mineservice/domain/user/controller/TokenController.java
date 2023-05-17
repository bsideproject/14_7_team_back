package com.mineservice.domain.user.controller;


import com.mineservice.domain.user.JwtTokenProvider;
import com.mineservice.domain.user.domain.MineKeyEntity;
import com.mineservice.domain.user.domain.UserInfoEntity;
import com.mineservice.domain.user.service.MineKeyService;
import com.mineservice.domain.user.service.UserInfoService;
import com.mineservice.domain.user.vo.RequestJwt;
import com.mineservice.domain.user.vo.response.ResponseJwt;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final MineKeyService mineKeyService;
    private final UserInfoService userInfoService;
    private final JwtTokenProvider provider;

    @PostMapping("/token/renew")
    @ApiOperation(value = "MINE 토큰 재발급")
    public ResponseEntity<ResponseJwt> renewToken(@RequestBody RequestJwt token) {

        Optional<MineKeyEntity> optionalMineKey = mineKeyService.findByToken(token.getAccessToken());
        if (optionalMineKey.isEmpty()) {
            log.error("저장된 키 정보가 없습니다");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String userId = optionalMineKey.get().getUserId();
        log.info("userId : {}", userId);

        Optional<UserInfoEntity> optionalUserInfo = userInfoService.findById(userId);
        if (optionalUserInfo.isEmpty()) {
            log.error("회원정보가 없습니다");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<String> roles = Collections.singletonList("ROLE_USER");
        String jwt = provider.generateJwt(userId, roles);
        ResponseJwt responseJwt = new ResponseJwt();
        responseJwt.setAccessToken(jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJwt);
    }

}
