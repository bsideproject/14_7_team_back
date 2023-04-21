package com.mineservice.domain.tag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TagResDTO {

    private Long id;
    private String name;

}
