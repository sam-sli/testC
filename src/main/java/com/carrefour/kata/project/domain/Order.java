package com.carrefour.kata.project.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record Order(
        UUID id,
        UUID customerId,
        List<CartItem> items,
        OrderStatus status,
        LocalDateTime createdAt
) {
    public enum OrderStatus {
        PENDING, CONFIRMED, CANCELLED, SHIPPED, DELIVERED
    }
}
