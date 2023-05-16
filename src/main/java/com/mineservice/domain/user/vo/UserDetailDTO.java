package com.mineservice.domain.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@ToString
@Schema(name = "유저정보")
public class UserDetailDTO {

    @Schema(name = "사용자명")
    private String userName;
    @Schema(name = "전체 아티클 개수")
    private int totalArticleSize;
    @Schema(name = "알람여부 (true : on | false : off)")
    private boolean alarm;
    @Schema(name = "화면에 보여질 알람시각", example = "매일 오전 09시 00분")
    private String displayAlarmTime;

    @Schema(name = "바텀시트를 위한 요일 리스트")
    private List<String> days;

    @Schema(name = "바텀시트를 위한 알람 시각", type = "LocalTime", example = "09:00:00")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime time;

}
