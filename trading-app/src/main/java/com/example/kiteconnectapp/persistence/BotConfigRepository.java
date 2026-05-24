package com.example.kiteconnectapp.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BotConfigRepository extends JpaRepository<BotConfigEntity, String> {
	List<BotConfigEntity> findByEnabledTrue();
}
