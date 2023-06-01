package com.mineservice.domain.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "회원 탈퇴 객체")
public class UserSignOutDTO {

    @Schema(description = "탈퇴사유종류", example = "unfrequentlyUsed")
    private String signOutType;
    @Schema(description = "기타 사유", example = "그냥")
    private String reason;

}
