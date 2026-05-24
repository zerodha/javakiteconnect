package com.example.kiteconnectapp.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BotLogRepository extends JpaRepository<BotLogEntity, Long> {
    List<BotLogEntity> findTop200ByOrderByIdDesc();
}
