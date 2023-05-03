package com.mineservice.login.vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
