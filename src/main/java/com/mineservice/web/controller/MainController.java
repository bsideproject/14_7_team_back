package com.mineservice.web.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
@Slf4j
@AllArgsConstructor
public class MainController {

    @RequestMapping("/")
    public String main() {
        log.info("index.html");
        return "/index.html";
    }
}
