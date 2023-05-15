package com.mineservice.domain.user.service;

import com.mineservice.domain.user.domain.UserAlarmEntity;
import com.mineservice.domain.user.repository.UserAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAlarmService {

    private final UserAlarmRepository userAlarmRepository;

    public void switchUserAlarm(boolean alarm, String userId) {
        Optional<UserAlarmEntity> optionalUserAlarm = userAlarmRepository.findById(userId);
        UserAlarmEntity userAlarm;
        if (optionalUserAlarm.isPresent()) {
            userAlarm = optionalUserAlarm.get();
            userAlarm.setNotiYn(alarm ? "Y" : "N");
        } else {
            userAlarm = UserAlarmEntity.builder()
                    .userId(userId)
                    .notiYn(alarm ? "Y" : "N")
                    .createBy(userId)
                    .createDt(LocalDateTime.now())
                    .build();
        }
        userAlarmRepository.save(userAlarm);
    }

}
