package com.example.kiteconnectapp.persistence;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bot_configs")
@Getter
@Setter
public class BotConfigEntity {

    @Id
    @Column(name = "bot_id", nullable = false, length = 64)
    private String botId;

    @Column(name = "name")
    private String name;

    @Column(name = "exchange_name")
    private String exchange;

    @Column(name = "trading_symbol")
    private String tradingSymbol;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "product_type")
    private String product;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "interval_seconds")
    private Integer intervalSeconds;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "dry_run")
    private Boolean dryRun;

    @Column(name = "max_orders_per_day")
    private Integer maxOrdersPerDay;

    @Column(name = "max_position_size")
    private Integer maxPositionSize;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;
}
