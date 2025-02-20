package com.carrefour.kata.project.ports.primary;

import com.carrefour.kata.project.domain.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CartService {
    Mono<Void> addToCart(UUID customerId, CartItem item);
    Mono<Void> removeFromCart(UUID customerId, UUID productId);
    Flux<CartItem> getCart(UUID customerId);
}


