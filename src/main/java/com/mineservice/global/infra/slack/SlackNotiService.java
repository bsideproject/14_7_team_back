package com.mineservice.global.infra.slack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackNotiService {

    @Value("${slack-web-hook-url}")
    private String slackNotiUrl;

    public void sendSlackNotify(String title, String msg) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("{\n" +
                "   \"attachments\":[\n" +
                "      {\n" +
                "         \"color\":\"#D00000\",\n" +
                "         \"fields\":[\n" +
                "            {\n" +
                "               \"title\":\"" + title + "\",\n" +
                "               \"value\":\"" + msg + "\",\n" +
                "               \"short\":true\n" +
                "            }\n" +
                "         ]\n" +
                "      }\n" +
                "   ]\n" +
                "}", headers);

        rt.postForObject(slackNotiUrl, entity, String.class);
    }

}
