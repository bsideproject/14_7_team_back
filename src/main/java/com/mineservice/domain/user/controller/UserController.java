package com.mineservice.domain.user.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class UserController {


    @GetMapping("/user")
    public ResponseEntity<String> userDetail(@ApiIgnore @AuthenticationPrincipal UserDetails user) {
        log.info("userId :{}", user.getUsername());
        

        return ResponseEntity.ok().build();
    }



    @DeleteMapping("/sign-out")
    @ApiOperation(value = "회원 탈퇴")
    public ResponseEntity<String> withdrawUser(@ApiIgnore @AuthenticationPrincipal UserDetails user) {
        log.info("userId :{}", user.getUsername());



        return ResponseEntity.ok().build();
    }


}
