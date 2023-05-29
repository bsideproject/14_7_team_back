package com.mineservice.domain.push.repository;

import com.mineservice.domain.push.domain.PushNoti;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PushNotiRepository extends JpaRepository<PushNoti, Long> {

    List<PushNoti> findAllByUserId(String userId);

    void deleteAllByUserId(String userId);

}
