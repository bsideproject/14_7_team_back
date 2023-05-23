package com.mineservice.domain.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "아티클 읽음 수정 요청 객체")
public class ArticleModReadDTO {

    @Schema(description = "아티클 id", example = "1")
    private Long articleId;

    @Schema(description = "열람여부", example = "false")
    private Boolean read;

}
