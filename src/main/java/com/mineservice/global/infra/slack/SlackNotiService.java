package com.mineservice.global.infra.slack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackNotiService {

    @Value("${slack-web-hook-url}")
    private String slackNotiUrl;


    public void sendSlackNotify() {

    }

}
