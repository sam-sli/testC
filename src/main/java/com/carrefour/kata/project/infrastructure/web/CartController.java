package com.carrefour.kata.project.infrastructure.web;

import com.carrefour.kata.project.domain.CartItem;
import com.carrefour.kata.project.ports.primary.CartService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public Mono<Void> addToCart(
            @RequestHeader("X-Customer-Id") UUID customerId,
            @RequestBody CartItem item
    ) {
        return cartService.addToCart(customerId, item);
    }

    @DeleteMapping("/{productId}")
    public Mono<Void> removeFromCart(
            @RequestHeader("X-Customer-Id") UUID customerId,
            @PathVariable UUID productId
    ) {
        return cartService.removeFromCart(customerId, productId);
    }

    @GetMapping
    public Flux<CartItem> getCart(@RequestHeader("X-Customer-Id") UUID customerId) {
        return cartService.getCart(customerId);
    }
}

