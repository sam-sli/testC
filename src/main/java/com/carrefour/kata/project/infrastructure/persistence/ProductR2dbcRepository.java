package com.carrefour.kata.project.infrastructure.persistence;


import com.carrefour.kata.project.infrastructure.entities.ProductEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProductR2dbcRepository extends R2dbcRepository<ProductEntity, UUID> {
    Mono<ProductEntity> findById(UUID id);
}

