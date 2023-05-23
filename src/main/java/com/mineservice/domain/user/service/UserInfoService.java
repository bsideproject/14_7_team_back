package com.mineservice.domain.user.service;

import com.mineservice.domain.user.domain.*;
import com.mineservice.domain.user.repository.AccessTokenRepository;
import com.mineservice.domain.user.repository.RefreshTokenRepository;
import com.mineservice.domain.user.repository.UserAlarmRepository;
import com.mineservice.domain.user.repository.UserInfoRepository;
import com.mineservice.domain.user.vo.UserDetailDTO;
import com.mineservice.domain.user.vo.UserInfo;
import com.mineservice.global.exception.CustomException;
import com.mineservice.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserAlarmRepository userAlarmRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final List<DayType> WEEK_DAY_LIST = Arrays.asList(DayType.MON, DayType.TUE, DayType.WED, DayType.THU, DayType.FRI);
    private static final List<DayType> WEEKEND_LIST = Arrays.asList(DayType.SAT, DayType.SUN);

    public UserDetailDTO findUserDetailDTO(String userId) {
        Optional<UserInfoEntity> optionalUserInfo = userInfoRepository.findById(userId);
        if (optionalUserInfo.isEmpty()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        UserInfoEntity userInfoEntity = optionalUserInfo.get();
        log.info("userInfoEntity : {}", userInfoEntity);

        UserDetailDTO detailDTO = new UserDetailDTO();
        detailDTO.setUserName(userInfoEntity.getEntityUserName());
        detailDTO.setTotalArticleSize(userInfoEntity.getArticle().size());

        UserAlarmEntity userAlarm = userInfoEntity.getUserAlarm();
        detailDTO.setAlarm("Y".equals(userAlarm.getNotiYn()));

        if ("Y".equals(userAlarm.getNotiYn())) {
            List<String> frequency = Arrays.stream(userAlarm.getFrequency().split(",")).collect(Collectors.toList());
            List<DayType> days = new ArrayList<>();
            for (String s : frequency) {
                switch (s) {
                    case "월":
                    case "화":
                    case "수":
                    case "목":
                    case "금":
                    case "토":
                    case "일":
                        days.add(DayType.get(s));
                        break;
                    case "평일":
                        days.addAll(WEEK_DAY_LIST);
                        break;
                    case "주말":
                        days.addAll(WEEKEND_LIST);
                        break;
                    case "매일":
                        days.addAll(WEEK_DAY_LIST);
                        days.addAll(WEEKEND_LIST);
                        break;
                    default:
                        break;
                }
            }
            String dispDay = frequency.stream()
                    .map(DayType::get)
                    .sorted(Comparator.comparing(dayType -> dayType != null ? dayType.getOrder() : 0))
                    .filter(Objects::nonNull)
                    .map(DayType::getCode)
                    .collect(Collectors.joining(","));

            detailDTO.setDays(days.stream().sorted(Comparator.comparing(DayType::getOrder)).map(DayType::getCode).collect(Collectors.toList()));
            detailDTO.setTime(userAlarm.getTime());
            detailDTO.setDisplayAlarmTime(dispDay + " " + userAlarm.getTime().format(DateTimeFormatter.ofPattern("a hh시 mm분")));
        }
        log.info("detailDTO : {}", detailDTO.toString());
        return detailDTO;
    }

    public void modifyUserName(String userId, String userName) {
        Optional<UserInfoEntity> optionalUserInfo = findById(userId);
        if (optionalUserInfo.isEmpty()) {
            log.error("해당하는 유저가 없습니다 : {}", userId);
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        UserInfoEntity userInfoEntity = optionalUserInfo.get();
        userInfoEntity.setUserName(userName);
        userInfoEntity.setModifyBy(userId);
        userInfoEntity.setModifyDt(LocalDateTime.now());
        userInfoRepository.save(userInfoEntity);
    }

    public Optional<UserInfoEntity> findById(String id) {
        return userInfoRepository.findById(id);
    }

    public UserInfoEntity findByIdAndProvider(String id, String provider) {
        return userInfoRepository.findByUidAndProvider(id, provider);
    }

    public UserInfoEntity joinUser(String userId, UserInfo userInfo) {

        AccessTokenEntity accessToken = AccessTokenEntity.builder()
                .userId(userId)
                .token(userInfo.getAccessToken())
                .expireDt(userInfo.getAccessTokenExpireDate())
                .createBy(userId)
                .build();

        accessTokenRepository.save(accessToken);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .userId(userId)
                .token(userInfo.getRefreshToken())
                .expireDt(userInfo.getAccessTokenExpireDate())
                .createBy(userId)
                .build();

        refreshTokenRepository.save(refreshToken);

        UserAlarmEntity userAlarm = UserAlarmEntity.builder()
                .userId(userId)
                .notiYn("N")
                .createBy(userId)
                .createDt(LocalDateTime.now())
                .build();
        userAlarmRepository.save(userAlarm);

        UserInfoEntity entity = UserInfoEntity.builder()
                .id(userId)
                .userName(userInfo.getName())
                .userEmail(userInfo.getEmail())
                .uid(userInfo.getId())
                .provider(userInfo.getProvider())
                .lastLoginDt(LocalDateTime.now())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .createDt(LocalDateTime.now())
                .createBy(userId)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        UserInfoEntity user = userInfoRepository.save(entity);
        return user;
    }

    public UserInfoEntity memberLogin(UserInfoEntity entity) {
        entity.setLastLoginDt(LocalDateTime.now());
        entity.setModifyBy(entity.getId());
        return userInfoRepository.save(entity);
    }
}
