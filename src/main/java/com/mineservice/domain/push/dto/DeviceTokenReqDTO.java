package com.mineservice.domain.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "device token 등록 객체")
public class DeviceTokenReqDTO {

    @ApiModelProperty(value = "푸시 토큰", example = "adsadsasd")
    private String deviceToken;

}
