package com.mineservice.domain.user.service;

import com.mineservice.domain.user.domain.UserAlarmEntity;
import com.mineservice.domain.user.repository.UserAlarmRepository;
import com.mineservice.domain.user.vo.UserAlarmReqDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAlarmService {

    private final UserAlarmRepository userAlarmRepository;

    private static final List<String> WEEK_DAY_LIST = Arrays.asList("월", "화", "수", "목", "금");
    private static final List<String> WEEKEND_LIST = Arrays.asList("토", "일");

    public Optional<String> modifyUserAlarm(UserAlarmReqDTO dto, String userId) {
        Boolean alarm = dto.getAlarm();

        Optional<UserAlarmEntity> optionalUserAlarm = userAlarmRepository.findById(userId);
        UserAlarmEntity userAlarm;
        if (optionalUserAlarm.isPresent()) {
            userAlarm = optionalUserAlarm.get();
            userAlarm.setNotiYn(Boolean.TRUE.equals(alarm) ? "Y" : "N");
        } else {
            userAlarm = UserAlarmEntity.builder()
                    .userId(userId)
                    .notiYn(Boolean.TRUE.equals(alarm) ? "Y" : "N")
                    .createBy(userId)
                    .createDt(LocalDateTime.now())
                    .build();
        }
        if (Boolean.TRUE.equals(alarm)) {
            List<String> days = dto.getDays();

            List<String> frequencys = new ArrayList<>();
            if (new HashSet<>(days).containsAll(WEEK_DAY_LIST) && new HashSet<>(days).containsAll(WEEKEND_LIST)) {
                frequencys.add("매일");
                days.removeAll(WEEK_DAY_LIST);
                days.removeAll(WEEKEND_LIST);
            }

            if (new HashSet<>(days).containsAll(WEEK_DAY_LIST)) {
                days.removeAll(WEEK_DAY_LIST);
                frequencys.add("평일");
            } else if (new HashSet<>(days).containsAll(WEEKEND_LIST)) {
                days.removeAll(WEEKEND_LIST);
                frequencys.add("주말");
            }

            if (!days.isEmpty()) {
                frequencys.addAll(days);
            }
            userAlarm.setFrequency(String.join(",",frequencys));
            userAlarm.setTime(dto.getTime());
        } else {
            userAlarm.setFrequency(null);
            userAlarm.setTime(null);
        }
        userAlarmRepository.save(userAlarm);
        log.info("userAlarm : {}", userAlarm.toString());

        if (Boolean.TRUE.equals(alarm)) {
            return Optional.of(userAlarm.getFrequency() + " " + userAlarm.getTime().format(DateTimeFormatter.ofPattern("a hh시 mm분")));
        }
        return Optional.empty();
    }

}
