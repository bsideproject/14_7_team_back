package com.mineservice.domain.user.controller;

import com.mineservice.domain.user.service.UserAlarmService;
import com.mineservice.domain.user.vo.UserAlarmReqDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class UserController {

    private final UserAlarmService userAlarmService;

    @GetMapping("/user/detail")
    public ResponseEntity<String> userDetail(@ApiIgnore @AuthenticationPrincipal UserDetails user) {
        log.info("userId :{}", user.getUsername());


        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/alarm")
    @ApiOperation(value = "회원 알람 on/off")
    public ResponseEntity<String> userAlarm(@RequestBody UserAlarmReqDTO alarmReqDTO,
                                            @ApiIgnore @AuthenticationPrincipal UserDetails user) {
        log.info("userId :{}", user.getUsername());
        log.info("alarmReqDTO : {}", alarmReqDTO);

        String userId = user.getUsername();
        userAlarmService.switchUserAlarm(alarmReqDTO.getAlarm(), userId);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/user/sign-out")
    @ApiOperation(value = "회원 탈퇴")
    public ResponseEntity<String> withdrawUser(@ApiIgnore @AuthenticationPrincipal UserDetails user) {
        log.info("userId :{}", user.getUsername());

        // 저장된 데이터 삭제 순서 정하기

        return ResponseEntity.ok().build();
    }


}
