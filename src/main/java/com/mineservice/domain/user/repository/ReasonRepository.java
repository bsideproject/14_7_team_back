package com.mineservice.domain.user.repository;

import com.mineservice.domain.user.domain.ReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReasonRepository extends JpaRepository<ReasonEntity, Integer> {

}
