package com.carrefour.kata.project.infrastructure.persistence;

import com.carrefour.kata.project.infrastructure.entities.OrderItemEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface OrderItemR2dbcRepository extends R2dbcRepository<OrderItemEntity, UUID> {
    Flux<OrderItemEntity> findByOrderId(UUID orderId);
}
