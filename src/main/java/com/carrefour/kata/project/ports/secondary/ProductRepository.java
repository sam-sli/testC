package com.carrefour.kata.project.ports.secondary;


import com.carrefour.kata.project.domain.Product;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductRepository {
    Mono<Product> findById(UUID id);
    Mono<Product> updateStock(UUID id, Integer quantity);
}

