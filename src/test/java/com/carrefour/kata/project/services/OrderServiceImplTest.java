package com.carrefour.kata.project.services;


import com.carrefour.kata.project.domain.CartItem;
import com.carrefour.kata.project.domain.Order;
import com.carrefour.kata.project.domain.Product;
import com.carrefour.kata.project.ports.primary.OrderService;
import com.carrefour.kata.project.ports.secondary.CartRepository;
import com.carrefour.kata.project.ports.secondary.OrderRepository;
import com.carrefour.kata.project.ports.secondary.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock private CartRepository cartRepository;
    @Mock private ProductRepository productRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(orderRepository, cartRepository, productRepository);
    }

    @Test
    void createOrder_ShouldSucceed() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        CartItem item = new CartItem(productId, 1);
        Product product = new Product(productId, "Test Product", BigDecimal.TEN, 5);

        when(cartRepository.getItems(customerId)).thenReturn(Flux.just(item));
        when(productRepository.findById(productId)).thenReturn(Mono.just(product));
        when(productRepository.updateStock(any(), any())).thenReturn(Mono.just(product));
        when(orderRepository.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

        StepVerifier.create(orderService.createOrder(customerId))
                .expectNextMatches(order ->
                        order.customerId().equals(customerId) &&
                                order.status() == Order.OrderStatus.PENDING &&
                                order.items().size() == 1
                )
                .verifyComplete();
    }

    @Test
    void cancelOrder_WithinTimeWindow_ShouldSucceed() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(
                orderId,
                UUID.randomUUID(),
                List.of(new CartItem(UUID.randomUUID(), 1)),
                Order.OrderStatus.PENDING,
                LocalDateTime.now()
        );

        when(orderRepository.findById(orderId)).thenReturn(Mono.just(order));
        when(orderRepository.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));
        when(productRepository.findById(any())).thenReturn(Mono.just(
                new Product(UUID.randomUUID(), "Test", BigDecimal.TEN, 5)
        ));
        when(productRepository.updateStock(any(), any())).thenReturn(Mono.just(
                new Product(UUID.randomUUID(), "Test", BigDecimal.TEN, 6)
        ));

        StepVerifier.create(orderService.cancelOrder(orderId))
                .expectNextMatches(cancelledOrder ->
                        cancelledOrder.status() == Order.OrderStatus.CANCELLED
                )
                .verifyComplete();
    }
}
