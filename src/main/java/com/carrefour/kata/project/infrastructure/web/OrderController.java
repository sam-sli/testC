package com.carrefour.kata.project.infrastructure.web;

import com.carrefour.kata.project.domain.Order;
import com.carrefour.kata.project.ports.primary.OrderService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Mono<Order> createOrder(@RequestHeader("X-Customer-Id") UUID customerId) {
        return orderService.createOrder(customerId);
    }

    @DeleteMapping("/{orderId}")
    public Mono<Order> cancelOrder(@PathVariable UUID orderId) {
        return orderService.cancelOrder(orderId);
    }

    @GetMapping("/{orderId}")
    public Mono<Order> getOrder(@PathVariable UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping
    public Flux<Order> getCustomerOrders(@RequestHeader("X-Customer-Id") UUID customerId) {
        return orderService.getCustomerOrders(customerId);
    }
}
