package com.example.mineservice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(tags = "테스트")
public class TestController {

    @GetMapping("/test")
    @ApiOperation(value = "테스트 API 입니다")
    public String test(){
        return "server started";
    }
}
