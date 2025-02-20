package com.carrefour.kata.project.domain;

import java.util.UUID;

public record CartItem(UUID productId, Integer quantity) {}
