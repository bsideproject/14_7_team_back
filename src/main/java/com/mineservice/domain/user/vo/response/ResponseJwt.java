package com.mineservice.domain.user.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseJwt {
    private String accessToken;
    private String type;
}
