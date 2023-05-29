package com.mineservice.domain.user.controller;

import com.mineservice.domain.user.service.UserAlarmService;
import com.mineservice.domain.user.service.UserInfoService;
import com.mineservice.domain.user.vo.UserAlarmReqDTO;
import com.mineservice.domain.user.vo.UserDetailDTO;
import com.mineservice.domain.user.vo.UserModifyDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class UserController {

    private final UserInfoService userInfoService;
    private final UserAlarmService userAlarmService;

    @GetMapping("/user/detail")
    @ApiOperation(value = "회원 정보 조회")
    @ApiResponses(
            @ApiResponse(responseCode = "404", description = "해당하는 회원정보가 없을경우")
    )
    public ResponseEntity<UserDetailDTO> userDetail(@ApiIgnore @AuthenticationPrincipal UserDetails user, HttpServletRequest request) {
        log.info("api : {}", request.getRequestURI());
        log.info("userId :{}", user.getUsername());

        UserDetailDTO userDetailDTO = userInfoService.findUserDetailDTO(user.getUsername());

        return ResponseEntity.ok(userDetailDTO);
    }

    @PutMapping("/user/name")
    @ApiOperation(value = "회원 이름 변경")
    @ApiResponses(
            @ApiResponse(responseCode = "404", description = "해당하는 회원정보가 없을경우")
    )
    public ResponseEntity<String> modifyUserName(@RequestBody UserModifyDTO dto,
                                                 @ApiIgnore @AuthenticationPrincipal UserDetails user,
                                                 HttpServletRequest request) {
        log.info("api : {}", request.getRequestURI());
        log.info("userId :{}", user.getUsername());
        log.info("userModifyDTO : {}", dto);

        String userId = user.getUsername();

        userInfoService.modifyUserName(userId, dto.getUserName());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/alarm")
    @ApiOperation(value = "회원 알람 설정")
    public ResponseEntity<String> userAlarm(@RequestBody UserAlarmReqDTO alarmReqDTO,
                                            @ApiIgnore @AuthenticationPrincipal UserDetails user,
                                            HttpServletRequest request) {
        log.info("api : {}", request.getRequestURI());
        log.info("userId :{}", user.getUsername());
        log.info("alarmReqDTO : {}", alarmReqDTO);

        String userId = user.getUsername();
        userAlarmService.modifyUserAlarm(alarmReqDTO, userId);

        return ResponseEntity.ok().build();
    }


    @PutMapping("/user/log-out")
    @ApiOperation(value = "회원 로그아웃")
    public ResponseEntity<String> userLogout(@ApiIgnore @AuthenticationPrincipal UserDetails user,
                                             HttpServletRequest request) {
        log.info("api : {}", request.getRequestURI());
        log.info("userId :{}", user.getUsername());

        String userId = user.getUsername();
        userInfoService.logOutUser(userId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/sign-out")
    @ApiOperation(value = "회원 탈퇴")
    public ResponseEntity<String> withdrawUser(@RequestParam String reason,
                                               @ApiIgnore @AuthenticationPrincipal UserDetails user,
                                               HttpServletRequest request) {
        log.info("api : {}", request.getRequestURI());
        log.info("userId :{}", user.getUsername());
        log.info("reason : {}", reason);

        String userId = user.getUsername();
        userInfoService.withdrawUser(userId, reason);

        return ResponseEntity.ok().build();
    }


}
