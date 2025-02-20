package com.carrefour.kata.project.infrastructure.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("cart_items")
public record CartItemEntity(
        @Id UUID id,
        UUID customerId,
        UUID productId,
        Integer quantity
) {}
