package com.mineservice.global.scheduler;

import com.mineservice.domain.article.domain.ArticleEntity;
import com.mineservice.domain.article.repository.ArticleRepository;
import com.mineservice.domain.user.domain.UserAlarmEntity;
import com.mineservice.domain.user.repository.UserAlarmRepository;
import com.mineservice.global.infra.apns.ApnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    private final ApnsService apnsService;
    private final ArticleRepository articleRepository;
    private final UserAlarmRepository userAlarmRepository;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void userAlarm() {
        log.info("userAlarm 실행");

        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        String today = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        LocalTime min = LocalTime.now().minusMinutes(1).withSecond(59).withNano(0);
        LocalTime max = LocalTime.now().plusMinutes(1).withSecond(0).withNano(0);

        List<UserAlarmEntity> userAlarmEntityList = userAlarmRepository.findAllByNotiYn("Y")
                .stream()
                .filter(ua -> ua.getTime().isAfter(min) && ua.getTime().isBefore(max))
                .collect(Collectors.toList());

        List<String> userIdList = filter(today, userAlarmEntityList);
        if (!userIdList.isEmpty()) {
            apnsService.sendUserPush(userIdList);
        }
    }

    public List<String> filter(String today, List<UserAlarmEntity> list) {
        switch (today) {
            case "월":
                return list.stream()
                        .filter(ua -> ua.getFrequency().contains("월") || ua.getFrequency().equals("매일") || ua.getFrequency().equals("평일"))
                        .map(UserAlarmEntity::getUserId)
                        .collect(Collectors.toList());
            case "화":
                return list.stream()
                        .filter(ua -> ua.getFrequency().contains("화") || ua.getFrequency().equals("매일") || ua.getFrequency().equals("평일"))
                        .map(UserAlarmEntity::getUserId)
                        .collect(Collectors.toList());
            case "수":
                return list.stream()
                        .filter(ua -> ua.getFrequency().contains("수") || ua.getFrequency().equals("매일") || ua.getFrequency().equals("평일"))
                        .map(UserAlarmEntity::getUserId)
                        .collect(Collectors.toList());
            case "목":
                return list.stream()
                        .filter(ua -> ua.getFrequency().contains("목") || ua.getFrequency().equals("매일") || ua.getFrequency().equals("평일"))
                        .map(UserAlarmEntity::getUserId)
                        .collect(Collectors.toList());
            case "금":
                return list.stream()
                        .filter(ua -> ua.getFrequency().contains("금") || ua.getFrequency().equals("매일") || ua.getFrequency().equals("평일"))
                        .map(UserAlarmEntity::getUserId)
                        .collect(Collectors.toList());
            case "토":
                return list.stream()
                        .filter(ua -> ua.getFrequency().contains("토") || ua.getFrequency().equals("매일") || ua.getFrequency().equals("주말"))
                        .map(UserAlarmEntity::getUserId)
                        .collect(Collectors.toList());
            case "일":
                return list.stream()
                        .filter(ua -> Arrays.asList(ua.getFrequency().split(",")).contains("일") || ua.getFrequency().equals("매일") || ua.getFrequency().equals("주말"))
                        .map(UserAlarmEntity::getUserId)
                        .collect(Collectors.toList());
            default:
                return new ArrayList<>();
        }


    }


    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void articleAlarm() {
        log.info("articleAlarm 실행");
        List<ArticleEntity> findAll = articleRepository.findAll();

        LocalDateTime min = LocalDateTime.now().minusMinutes(1).withSecond(59).withNano(0);
        LocalDateTime max = LocalDateTime.now().plusMinutes(1).withSecond(0).withNano(0);

        List<ArticleEntity> nowAlarm = findAll.stream()
                .filter(a -> a.getArticleAlarm() != null)
                .filter(a -> a.getArticleAlarm().getTime().isAfter(min) && a.getArticleAlarm().getTime().isBefore(max))
                .collect(Collectors.toList());

        if (!nowAlarm.isEmpty()) {
            apnsService.sendArticlePush(nowAlarm);
        }

    }


}
