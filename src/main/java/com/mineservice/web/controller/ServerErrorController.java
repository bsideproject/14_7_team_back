/*
 *  Copyright (c) 2023 MIBANK, Inc.
 *  All right reserved.
 *  This software is the confidential and proprietary information of MIBANK
 *  , Inc. You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with MIBANK.
 *
 *  Revision History
 *  Author Date Description
 *  ------------------ -------------- ------------------
 *  jason 23. 2. 15. 오후 2:25
 */

package com.mineservice.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequiredArgsConstructor
@Slf4j
@ApiIgnore
public class ServerErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {

        String orgUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        log.error("orgUri : {}", orgUri);
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        Exception e = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (e != null) {
            log.error(e.getMessage());
        }

        if ("404".equals(status.toString())) {
            return "/view/error/404.html";
        } else if ("500".equals(status.toString())) {
            return "/view/error/500.html";
        }

        return "/view/error/error.html";
    }
}
