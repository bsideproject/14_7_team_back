package com.mineservice.domain.push.application;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@SpringBootTest
class PushNotiServiceTest {

    @Test
    void pushTest() throws IOException {

        Resource resource = new ClassPathResource("static/apple/ApplePushServicesMine.p12");

        ApnsClient apnsClient = new ApnsClientBuilder()
                .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                .setClientCredentials(resource.getFile(), "")
                .build();

        String payload = new SimpleApnsPayloadBuilder()
                .setAlertTitle("title")
                .setAlertBody("body")
                .addCustomProperty("pushType", "article1")
                .build();

        String token = TokenUtil.sanitizeTokenString("f31f0afad3843c00e1fe719f1d78e1f5c0728e8b9f075c55cef82216489ab094");

        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, "com.bside.Mine", payload);

        PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification);

        try {
            PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();
            if (pushNotificationResponse.isAccepted()) {
                System.out.println("Push 성공");
            } else {
                System.out.println("Push 실패 " + pushNotificationResponse.getRejectionReason());

                pushNotificationResponse.getTokenInvalidationTimestamp().ifPresent(timestamp -> {
                    System.out.println("\t…and the token is invalid as of " + timestamp);
                });
            }
        } catch (Exception e) {
            System.err.println("Push 실패");
            e.printStackTrace();
        }
    }

}