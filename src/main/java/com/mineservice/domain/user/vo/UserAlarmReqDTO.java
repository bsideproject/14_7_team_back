package com.mineservice.domain.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "회원 알람 on/off 를 위한 객체")
public class UserAlarmReqDTO {

    @ApiModelProperty(value = "알람 on/off (true : on | false : off)", example = "true")
    boolean alarm;

}
