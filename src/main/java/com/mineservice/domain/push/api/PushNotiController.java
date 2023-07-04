package com.mineservice.domain.push.api;

import com.mineservice.domain.action.application.ActionHistService;
import com.mineservice.domain.push.application.DeviceTokenService;
import com.mineservice.domain.push.application.PushNotiService;
import com.mineservice.domain.push.dto.DeviceTokenReqDTO;
import com.mineservice.domain.push.dto.PushNotiResDTO;
import com.mineservice.domain.push.dto.TouchPushReqDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class PushNotiController {

    private final PushNotiService pushNotiService;
    private final DeviceTokenService deviceTokenService;
    private final ActionHistService actionHistService;

    @GetMapping("/push-noti")
    @ApiOperation(value = "알림 목록 불러오기")
    public ResponseEntity<List<PushNotiResDTO>> getNotiList(@ApiIgnore @AuthenticationPrincipal UserDetails user,
                                                            HttpServletRequest request) {
        log.info("api : {}", request.getRequestURI());
        if (user == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        log.info("userId : {}", user.getUsername());
        String userId = user.getUsername();

        List<PushNotiResDTO> notiList = pushNotiService.findAllByUserId(userId);

        return ResponseEntity.ok(notiList);
    }


    @PostMapping("/device-token")
    @ApiOperation(value = "푸시 발송을 위한 토큰 등록")
    public ResponseEntity<String> registerDeviceToken(@RequestBody DeviceTokenReqDTO tokenReqDTO,
                                                      @ApiIgnore @AuthenticationPrincipal UserDetails user,
                                                      HttpServletRequest request) {
        log.info("api : {}", request.getRequestURI());
        log.info("userId : {}", user.getUsername());
        log.info("tokenReqDTO : {}", tokenReqDTO);
        String userId = user.getUsername();

        deviceTokenService.registerToken(tokenReqDTO.getDeviceToken(), userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/touch-push")
    @ApiOperation(value = "푸시 노티 클릭 기록")
    public ResponseEntity<String> touchPush(@RequestBody TouchPushReqDTO touchPushReqDTO,
                                                HttpServletRequest request) {
        log.info("api : {}", request.getRequestURI());
        log.info("touchPushReqDTO : {}", touchPushReqDTO);

        actionHistService.createHist(null, "push", touchPushReqDTO.getPushType());

        return ResponseEntity.ok().build();
    }


}
