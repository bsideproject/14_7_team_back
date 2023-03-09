package com.example.mineservice;

import com.example.mineservice.config.NaverConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(tags = "테스트")
@RequiredArgsConstructor
public class TestController {

    private final NaverConfig naverConfig;

    @GetMapping("/test")
    @ApiOperation(value = "테스트 API 입니다")
    public String test() {
        return "server started";
    }

    @GetMapping("/naver-key")
    @ApiOperation(value = "네이버 키 가져오기")
    public ResponseEntity<String> naverKey() {
        return new ResponseEntity<>(naverConfig.getNaverKey(), HttpStatus.OK);
    }


    @GetMapping("/404")
    public ResponseEntity<String> notFound() {
        return new ResponseEntity<>("404", HttpStatus.NOT_FOUND);
    }

}
