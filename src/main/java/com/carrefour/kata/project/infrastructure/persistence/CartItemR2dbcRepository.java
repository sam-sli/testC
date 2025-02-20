package com.carrefour.kata.project.infrastructure.persistence;

import com.carrefour.kata.project.infrastructure.entities.CartItemEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CartItemR2dbcRepository extends R2dbcRepository<CartItemEntity, UUID> {
    Flux<CartItemEntity> findByCustomerId(UUID customerId);
    Mono<Void> deleteByCustomerIdAndProductId(UUID customerId, UUID productId);
}
