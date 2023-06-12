package com.mineservice.domain.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "push 클릭 기록")
public class TouchPushReqDTO {

    @ApiModelProperty(value = "푸시타입", example = "user1")
    private String pushType;

}
