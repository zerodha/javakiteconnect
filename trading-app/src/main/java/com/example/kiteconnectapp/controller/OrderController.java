package com.example.kiteconnectapp.controller;

import com.example.kiteconnectapp.dto.OrderRequest;
import com.example.kiteconnectapp.service.OrderService;
import com.zerodhatech.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(request));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> modifyOrder(@PathVariable String orderId, @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.modifyOrder(orderId, request));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId, @RequestParam(required = false) String variety) {
        return ResponseEntity.ok(Collections.singletonMap("orderId", orderService.cancelOrder(orderId, variety).orderId));
    }
}
