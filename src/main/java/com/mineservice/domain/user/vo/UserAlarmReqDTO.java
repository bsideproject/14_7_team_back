package com.mineservice.domain.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "회원 알람 on/off 를 위한 객체")
public class UserAlarmReqDTO {

    @Schema(description = "알람 on/off (true : on | false : off)", example = "true")
    private Boolean alarm;

}
