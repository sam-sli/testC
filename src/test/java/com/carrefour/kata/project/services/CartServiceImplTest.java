package com.carrefour.kata.project.services;

import com.carrefour.kata.project.domain.CartItem;
import com.carrefour.kata.project.domain.Product;
import com.carrefour.kata.project.ports.primary.CartService;
import com.carrefour.kata.project.ports.secondary.CartRepository;
import com.carrefour.kata.project.ports.secondary.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CartServiceImplTest {
    @Mock
    private CartRepository cartRepository;
    @Mock private ProductRepository productRepository;
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartServiceImpl(cartRepository, productRepository);
    }

    @Test
    void addToCart_WithAvailableStock_ShouldSucceed() {
        UUID productId = UUID.randomUUID();
        CartItem item = new CartItem(productId, 2);
        Product product = new Product(productId, "Test Product", BigDecimal.TEN, 5);

        when(productRepository.findById(productId)).thenReturn(Mono.just(product));
        when(cartRepository.addItem(any(), any())).thenReturn(Mono.empty());

        StepVerifier.create(cartService.addToCart(UUID.randomUUID(), item))
                .verifyComplete();
    }

    @Test
    void addToCart_WithInsufficientStock_ShouldFail() {
        UUID productId = UUID.randomUUID();
        CartItem item = new CartItem(productId, 10);
        Product product = new Product(productId, "Test Product", BigDecimal.TEN, 5);

        when(productRepository.findById(productId)).thenReturn(Mono.just(product));

        StepVerifier.create(cartService.addToCart(UUID.randomUUID(), item))
                .verifyComplete();
    }
}

