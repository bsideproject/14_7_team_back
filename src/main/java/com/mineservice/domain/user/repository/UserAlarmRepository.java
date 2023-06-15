package com.mineservice.domain.user.repository;

import com.mineservice.domain.user.domain.UserAlarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAlarmRepository extends JpaRepository<UserAlarmEntity, String> {

    void deleteByUserId(String userId);


    @Query(value = "SELECT u.* FROM user_alarm u WHERE u.noti_yn = :notiYn", nativeQuery = true)
    List<UserAlarmEntity> findAllByNotiYn(@Param("notiYn") String notiYn);

}
