package com.carrefour.kata.project.infrastructure.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("products")
public record ProductEntity(
        @Id UUID id,
        String name,
        BigDecimal price,
        Integer stockQuantity
) {}

