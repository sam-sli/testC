package com.carrefour.kata.project.ports.primary;

import com.carrefour.kata.project.domain.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderService {
    Mono<Order> createOrder(UUID customerId);
    Mono<Order> cancelOrder(UUID orderId);
    Mono<Order> getOrder(UUID orderId);
    Flux<Order> getCustomerOrders(UUID customerId);
}
