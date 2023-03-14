package com.mineservice.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class MainController {

    @RequestMapping("/")
    public String main() {
        return "/index.html";
    }

}
