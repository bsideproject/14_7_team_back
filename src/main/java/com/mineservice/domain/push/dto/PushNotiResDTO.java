package com.mineservice.domain.push.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PushNotiResDTO {

    private String type;
    private String title;
    private String notiDate;
    private Long articleId;
    private String articleUrl;


}
