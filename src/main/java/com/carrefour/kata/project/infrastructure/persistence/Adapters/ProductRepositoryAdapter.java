package com.carrefour.kata.project.infrastructure.persistence.Adapters;

import com.carrefour.kata.project.domain.Product;
import com.carrefour.kata.project.infrastructure.entities.ProductEntity;
import com.carrefour.kata.project.infrastructure.persistence.ProductR2dbcRepository;
import com.carrefour.kata.project.ports.secondary.ProductRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class ProductRepositoryAdapter implements ProductRepository {
    private final ProductR2dbcRepository repository;

    public ProductRepositoryAdapter(ProductR2dbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Product> findById(UUID id) {
        return repository.findById(id)
                .map(entity -> new Product(
                        entity.id(),
                        entity.name(),
                        entity.price(),
                        entity.stockQuantity()
                ));
    }

    @Override
    public Mono<Product> updateStock(UUID id, Integer quantity) {
        return repository.findById(id)
                .flatMap(entity -> {
                    var updated = new ProductEntity(
                            entity.id(),
                            entity.name(),
                            entity.price(),
                            quantity
                    );
                    return repository.save(updated);
                })
                .map(entity -> new Product(
                        entity.id(),
                        entity.name(),
                        entity.price(),
                        entity.stockQuantity()
                ));
    }
}

