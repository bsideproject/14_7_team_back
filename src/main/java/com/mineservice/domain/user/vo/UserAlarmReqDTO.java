package com.mineservice.domain.user.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
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
@Schema(description = "회원 알람 on/off 를 위한 객체")
public class UserAlarmReqDTO {

    @Schema(description = "알람 on/off (true : on | false : off)", example = "true")
    private Boolean alarm;

    @Schema(description = "알람요일", example = "월,화,수,목,금,토,일")
    private List<String> days;

    @Schema(description = "알람시각", example = "09:00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime time;

}
