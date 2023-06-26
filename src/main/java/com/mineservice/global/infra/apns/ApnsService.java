package com.mineservice.global.infra.apns;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import com.mineservice.domain.article.application.ArticleService;
import com.mineservice.domain.article.domain.ArticleEntity;
import com.mineservice.domain.push.application.DeviceTokenService;
import com.mineservice.domain.push.domain.DeviceToken;
import com.mineservice.domain.push.domain.PushNoti;
import com.mineservice.domain.push.repository.PushNotiRepository;
import com.mineservice.domain.user.domain.UserInfoEntity;
import com.mineservice.domain.user.service.UserInfoService;
import com.mineservice.global.infra.slack.SlackNotiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApnsService {

    private final UserInfoService userInfoService;
    private final ArticleService articleService;
    private final DeviceTokenService deviceTokenService;
    private final PushNotiRepository pushNotiRepository;
    private final SlackNotiService notify;


    Resource resource = new ClassPathResource("static/apple/ApplePushServicesMine.p12");


    public void sendUserPush(List<String> userIdList) {
        int success = 0;
        int fail = 0;

        for (String userId : userIdList) {
            log.info("userId : {}", userId);
            List<ArticleEntity> articleList = articleService.findAllByUserId(userId);
            List<ArticleEntity> unreadList = articleList.stream().filter(ar -> ar.getReadYn().equals("N")).collect(Collectors.toList());
            if (unreadList.isEmpty()) {
                log.info("미확인 콘텐츠 없음");
                continue;
            }

            Optional<DeviceToken> optionalDeviceToken = deviceTokenService.findByUserId(userId);
            if (optionalDeviceToken.isEmpty()) {
                log.info("deviceToken 없음");
                continue;
            }

            DeviceToken deviceToken = optionalDeviceToken.get();

            String payload = new SimpleApnsPayloadBuilder()
                    .setAlertTitle(String.format("%s님, 마인할 시간이예요!", articleList.size()))
                    .setAlertBody(String.format("마인에서 %s개의 콘텐츠를 확인해 보세요", unreadList.size()))
                    .addCustomProperty("pushType", "user2")
                    .build();

            if (sendPush(deviceToken.getToken(), payload)) {
                success++;
            } else {
                fail++;
            }
        }

        if (success + fail == 0) {
            log.info("푸시 대상이 없습니다");
            return;
        }

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm]"));
        notify.sendSlackNotify(now + "유저 푸시 발송", String.format("총 발송 : %s / 성공 : %s / 실패 : %s", success + fail, success, fail));
        log.info("{} 유저 푸시 발송 [Total : {} / Success : {} / Fail : {}]", now, success + fail, success, fail);
    }

    public void sendArticlePush(List<ArticleEntity> articleList) {

        int success = 0;
        int fail = 0;

        for (ArticleEntity article : articleList) {
            log.info("articleId : {}", article.getId());

            Optional<UserInfoEntity> optionalUserInfo = userInfoService.findById(article.getUserId());
            if (optionalUserInfo.isEmpty()) {
                log.info("userInfo 없음");
                continue;
            }

            UserInfoEntity userInfoEntity = optionalUserInfo.get();

            Optional<DeviceToken> optionalDeviceToken = deviceTokenService.findByUserId(article.getUserId());
            if (optionalDeviceToken.isEmpty()) {
                log.info("deviceToken 없음");
                continue;
            }

            DeviceToken deviceToken = optionalDeviceToken.get();

            String payload = new SimpleApnsPayloadBuilder()
                    .setAlertTitle(String.format("%s님, 마인에서 챙겨드려요", userInfoEntity.getEntityUserName()))
                    .setAlertBody(article.getTitle())
                    .addCustomProperty("pushType", "article2")
                    .addCustomProperty("articleId", article.getId())
                    .build();

            if (sendPush(deviceToken.getToken(), payload)) {
                pushNotiRepository.save(PushNoti.builder()
                        .userId(userInfoEntity.getId())
                        .articleId(article.getId())
                        .readYn("N")
                        .createdDate(LocalDate.now())
                        .createdDt(LocalDateTime.now())
                        .build());

                success++;
            } else {
                fail++;
            }
        }

        if (success + fail == 0) {
            log.info("푸시 대상이 없습니다");
            return;
        }

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm]"));
        notify.sendSlackNotify(now + "아티클 푸시 발송", String.format("총 발송 : %s / 성공 : %s / 실패 : %s", success + fail, success, fail));
        log.info("{} 아티클 푸시 발송 [Total : {} / Success : {} / Fail : {}]", now, success + fail, success, fail);
    }


    public boolean sendPush(String pushToken, String payload) {

        try {
            ApnsClient apnsClient = new ApnsClientBuilder()
                    .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                    .setClientCredentials(resource.getFile(), "")
                    .build();

            String token = TokenUtil.sanitizeTokenString(pushToken);

            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, "com.bside.Mine", payload);

            PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification);

            PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();
            if (pushNotificationResponse.isAccepted()) {
                log.info("push 발송 성공");
                return true;
            } else {
                log.error("Push 실패 " + pushNotificationResponse.getRejectionReason());

                pushNotificationResponse.getTokenInvalidationTimestamp().ifPresent(timestamp -> {
                    log.error("\t…and the token is invalid as of " + timestamp);
                });
                return false;
            }
        } catch (Exception e) {
            log.error("Push 실패");
            e.printStackTrace();
            return false;
        }

    }

}
