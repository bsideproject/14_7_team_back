package com.mineservice.domain.article.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "아티클 수정 요청 객체")
public class ArticleModDTO {

    @ApiModelProperty(value = "아티클 id", example = "1")
    private Long articleId;

    @ApiModelProperty(value = "제목", example = "테스트")
    private String title;

    @ApiModelProperty(value = "즐겨찾기", example = "false")
    private Boolean favorite;

    @ApiModelProperty(value = "열람여부", example = "false")
    private Boolean read;

    @ApiModelProperty(value = "알람여부", example = "false")
    private Boolean alarm;

    @ApiModelProperty(value = "알람시각(2019-01-31T11:30:59.000Z)", example = "2023-03-15 10:30")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime alarmTime;

    @ApiModelProperty(value = "태그리스트")
    private List<String> tags;


}
