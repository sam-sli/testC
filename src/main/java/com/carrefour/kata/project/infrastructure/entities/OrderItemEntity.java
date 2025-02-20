package com.carrefour.kata.project.infrastructure.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("order_items")
public record OrderItemEntity(
        @Id UUID id,
        UUID orderId,
        UUID productId,
        Integer quantity
) {}
