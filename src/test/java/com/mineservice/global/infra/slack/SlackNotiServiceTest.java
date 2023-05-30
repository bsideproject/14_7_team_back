package com.mineservice.global.infra.slack;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SpringBootTest
class SlackNotiServiceTest {

    @Test
    void slackNoti() {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String title = "타타타타타타타";
        String msg = "메메메메메메메메";

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

        rt.postForObject("https://hooks.slack.com/services/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", entity, String.class);
    }

}