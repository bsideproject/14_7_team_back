package com.mineservice.global.scheduler;

import com.mineservice.domain.article.application.ArticleService;
import com.mineservice.domain.article.domain.ArticleEntity;
import com.mineservice.domain.article.repository.ArticleRepository;
import com.mineservice.global.infra.slack.SlackNotiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    private final SlackNotiService notify;
    private final ArticleRepository articleRepository;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    @Async
    public void userAlarm() {
        log.info("userAlarm 실행");
        notify.sendSlackNotify("사용자 푸시 발송", 0 + "개 발송");
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    @Async
    public void articleAlarm() {
        log.info("articleAlarm 실행");
        List<ArticleEntity> findAll = articleRepository.findAll();
        log.info("findAllSize : {}", findAll.size());
        List<ArticleEntity> useAlarm = findAll.stream().filter(a -> a.getArticleAlarm() != null).collect(Collectors.toList());
        log.info("useAlarmSize : {}", useAlarm.size());
        List<ArticleEntity> nowAlarm = useAlarm.stream().filter(a -> a.getArticleAlarm().getTime().equals(LocalDateTime.now())).collect(Collectors.toList());
        log.info("nowAlarmSize : {}", nowAlarm.size());

        notify.sendSlackNotify("아티클 푸시 발송", nowAlarm.size() + "개 발송");

    }


}
