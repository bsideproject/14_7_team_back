package com.mineservice.domain.tag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class TagResDTO {

    private List<String> selectedTags;
    private List<String> tags;

}
