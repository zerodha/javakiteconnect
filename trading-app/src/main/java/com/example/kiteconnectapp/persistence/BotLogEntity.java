package com.example.kiteconnectapp.persistence;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bot_logs")
@Getter
@Setter
public class BotLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bot_id", nullable = false, length = 64)
    private String botId;

    @Column(name = "action_name")
    private String action;

    @Column(name = "status_name")
    private String status;

    @Column(name = "detail", length = 2000)
    private String detail;

    @Column(name = "created_at", nullable = false)
    private Instant timestamp;
}
