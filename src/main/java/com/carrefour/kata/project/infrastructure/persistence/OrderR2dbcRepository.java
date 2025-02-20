package com.carrefour.kata.project.infrastructure.persistence;

import com.carrefour.kata.project.infrastructure.entities.OrderEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface OrderR2dbcRepository extends R2dbcRepository<OrderEntity, UUID> {
    Flux<OrderEntity> findByCustomerId(UUID customerId);
}
