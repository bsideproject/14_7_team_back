package com.mineservice.domain.push.application;

import com.mineservice.domain.push.domain.DeviceToken;
import com.mineservice.domain.push.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    public void registerToken(String deviceToken, String userId) {

        Optional<DeviceToken> optionalDeviceToken = deviceTokenRepository.findByUserId(userId);
        optionalDeviceToken.ifPresent(deviceTokenRepository::delete);

        DeviceToken token = DeviceToken.builder()
                .token(deviceToken)
                .userId(userId)
                .failCount(0)
                .useYn("Y")
                .createBy(userId)
                .createDt(LocalDateTime.now())
                .build();

        deviceTokenRepository.save(token);
    }


}
