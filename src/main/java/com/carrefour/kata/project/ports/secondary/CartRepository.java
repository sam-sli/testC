package com.carrefour.kata.project.ports.secondary;

import com.carrefour.kata.project.domain.CartItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CartRepository {
    Mono<Void> addItem(UUID customerId, CartItem item);
    Mono<Void> removeItem(UUID customerId, UUID productId);
    Flux<CartItem> getItems(UUID customerId);
}
