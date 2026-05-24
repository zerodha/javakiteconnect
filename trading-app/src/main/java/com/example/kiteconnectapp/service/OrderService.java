package com.example.kiteconnectapp.service;

import com.example.kiteconnectapp.dto.OrderRequest;
import com.example.kiteconnectapp.exception.TradingAppException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private KiteClientService kiteClientService;

    public Order placeOrder(OrderRequest request) {
        try {
            KiteConnect client = kiteClientService.getAuthenticatedClient();
            OrderParams params = toOrderParams(request);
            String variety = request.getVariety() == null ? Constants.VARIETY_REGULAR : request.getVariety();
            return client.placeOrder(params, variety);
        } catch (KiteException | IOException e) {
            throw new TradingAppException("ORDER_PLACE_FAILED", e.getMessage(), e);
        }
    }

    public Order modifyOrder(String orderId, OrderRequest request) {
        try {
            KiteConnect client = kiteClientService.getAuthenticatedClient();
            OrderParams params = toOrderParams(request);
            String variety = request.getVariety() == null ? Constants.VARIETY_REGULAR : request.getVariety();
            return client.modifyOrder(orderId, params, variety);
        } catch (KiteException | IOException e) {
            throw new TradingAppException("ORDER_MODIFY_FAILED", e.getMessage(), e);
        }
    }

    public Order cancelOrder(String orderId, String variety) {
        try {
            KiteConnect client = kiteClientService.getAuthenticatedClient();
            String orderVariety = variety == null ? Constants.VARIETY_REGULAR : variety;
            return client.cancelOrder(orderId, orderVariety);
        } catch (KiteException | IOException e) {
            throw new TradingAppException("ORDER_CANCEL_FAILED", e.getMessage(), e);
        }
    }

    public List<Order> getOrders() {
        try {
            return kiteClientService.getAuthenticatedClient().getOrders();
        } catch (KiteException | IOException e) {
            throw new TradingAppException("ORDERS_FETCH_FAILED", e.getMessage(), e);
        }
    }

    private OrderParams toOrderParams(OrderRequest request) {
        if (request.getTradingSymbol() == null || request.getExchange() == null || request.getTransactionType() == null) {
            throw new TradingAppException("INVALID_ORDER", "exchange, tradingSymbol and transactionType are required.");
        }
        OrderParams params = new OrderParams();
        params.exchange = request.getExchange();
        params.tradingsymbol = request.getTradingSymbol();
        params.transactionType = request.getTransactionType();
        params.quantity = request.getQuantity();
        params.price = request.getPrice();
        params.product = request.getProduct();
        params.orderType = request.getOrderType();
        params.validity = request.getValidity();
        params.disclosedQuantity = request.getDisclosedQuantity();
        params.triggerPrice = request.getTriggerPrice();
        params.tag = request.getTag();
        return params;
    }
}
