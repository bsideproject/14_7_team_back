package com.mineservice.login.vo;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private String id;
    private String accessToken;
    private String refreshToken;
    private String name;
    private String email;
    private LocalDateTime accessTokenExpireDate;
    private String provider;
}
