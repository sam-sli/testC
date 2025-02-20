package com.carrefour.kata.project.infrastructure.persistence.Adapters;

import com.carrefour.kata.project.domain.CartItem;
import com.carrefour.kata.project.infrastructure.entities.CartItemEntity;
import com.carrefour.kata.project.infrastructure.persistence.CartItemR2dbcRepository;
import com.carrefour.kata.project.ports.secondary.CartRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CartRepositoryAdapter implements CartRepository {
    private final CartItemR2dbcRepository repository;

    public CartRepositoryAdapter(CartItemR2dbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Void> addItem(UUID customerId, CartItem item) {
        CartItemEntity entity = new CartItemEntity(
                UUID.randomUUID(),
                customerId,
                item.productId(),
                item.quantity()
        );
        return repository.save(entity).then();
    }

    @Override
    public Mono<Void> removeItem(UUID customerId, UUID productId) {
        return repository.deleteByCustomerIdAndProductId(customerId, productId);
    }

    @Override
    public Flux<CartItem> getItems(UUID customerId) {
        return repository.findByCustomerId(customerId)
                .map(entity -> new CartItem(entity.productId(), entity.quantity()));
    }
}
