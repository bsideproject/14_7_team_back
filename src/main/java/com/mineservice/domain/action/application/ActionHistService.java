package com.mineservice.domain.action.application;

import com.mineservice.domain.action.domain.ActionHistEntity;
import com.mineservice.domain.action.repository.ActionHistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionHistService {

    private final ActionHistRepository repository;


    public void createHist(String userId, String type, String detail) {
        ActionHistEntity hist = ActionHistEntity.builder()
                .userId(userId)
                .type(type)
                .detail(detail)
                .regDt(LocalDateTime.now())
                .build();
        repository.save(hist);
        log.info("insert action hist : {}", hist.toString());
    }


}
