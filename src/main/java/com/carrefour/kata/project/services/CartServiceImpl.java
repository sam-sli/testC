package com.carrefour.kata.project.services;

import com.carrefour.kata.project.domain.CartItem;
import com.carrefour.kata.project.ports.primary.CartService;
import com.carrefour.kata.project.ports.secondary.CartRepository;
import com.carrefour.kata.project.ports.secondary.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Void> addToCart(UUID customerId, CartItem item) {
        return productRepository.findById(item.productId())
                .filter(product -> product.stockQuantity() >= item.quantity())
                .flatMap(product -> cartRepository.addItem(customerId, item));
    }

    @Override
    public Mono<Void> removeFromCart(UUID customerId, UUID productId) {
        return cartRepository.removeItem(customerId, productId);
    }

    @Override
    public Flux<CartItem> getCart(UUID customerId) {
        return cartRepository.getItems(customerId);
    }
}

