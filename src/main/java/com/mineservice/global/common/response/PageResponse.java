package com.mineservice.global.common.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> extends CommonResponse {

    private Page<T> list;
}
