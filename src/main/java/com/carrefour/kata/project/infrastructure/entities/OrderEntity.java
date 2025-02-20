package com.carrefour.kata.project.infrastructure.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("orders")
public record OrderEntity(
        @Id UUID id,
        UUID customerId,
        String status,
        LocalDateTime createdAt
) {}
