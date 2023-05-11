package com.mineservice.domain.push.repository;

import com.mineservice.domain.push.domain.PushNotiHist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushNotiHistRepository extends JpaRepository<PushNotiHist, Long> {
}
