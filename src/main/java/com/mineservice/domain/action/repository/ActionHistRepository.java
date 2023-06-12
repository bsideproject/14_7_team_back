package com.mineservice.domain.action.repository;

import com.mineservice.domain.action.domain.ActionHistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionHistRepository extends JpaRepository<ActionHistEntity, Long> {
}
