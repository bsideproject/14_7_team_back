package com.mineservice.domain.article.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "img")
@ApiModel(value = "아티클 등록 요청 객체")
public class ArticleReqDTO {

    @ApiModelProperty(value = "제목", example = "테스트")
    private String title = null;

    @ApiModelProperty(value = "링크", example = "https://www.naver.com")
    private String url = null;

    @ApiModelProperty(value = "이미지", example = "image.png")
    private MultipartFile img = null;

    @ApiModelProperty(value = "즐겨찾기", example = "false")
    private boolean favorite = false;

    @ApiModelProperty(value = "태그리스트", example = "[태그1, 태그2]")
    private List<String> tags = new ArrayList<>();

    @ApiModelProperty(value = "알람시각(2019-01-31T11:30:59.000Z)", example = "2023-03-15 10:30")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime alarmTime = null;

    @ApiModelProperty(value = "등록방식(app|share)", example = "앱|공유하기")
    private String regType;

}
