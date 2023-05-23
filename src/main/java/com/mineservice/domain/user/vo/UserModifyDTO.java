package com.mineservice.domain.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "회원 이름변경을 위한 객체")
public class UserModifyDTO {

    @Schema(description = "변경할 회원 이름")
    private String userName;

}
