package com.mineservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class NaverConfig {
    @Value("${naver-key}")
    private String naverKey;

}
