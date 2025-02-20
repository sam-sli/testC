package com.carrefour.kata.project.services;

import com.carrefour.kata.project.domain.Order;
import com.carrefour.kata.project.ports.primary.OrderService;
import com.carrefour.kata.project.ports.secondary.CartRepository;
import com.carrefour.kata.project.ports.secondary.OrderRepository;
import com.carrefour.kata.project.ports.secondary.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Order> createOrder(UUID customerId) {
        return cartRepository.getItems(customerId)
                .collectList()
                .flatMap(items -> {
                    Order order = new Order(
                            UUID.randomUUID(),
                            customerId,
                            items,
                            Order.OrderStatus.PENDING,
                            LocalDateTime.now()
                    );
                    return orderRepository.save(order);
                })
                .flatMap(order -> updateInventory(order).thenReturn(order));
    }

    @Override
    public Mono<Order> cancelOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .filter(order ->
                        order.status() == Order.OrderStatus.PENDING &&
                                order.createdAt().plusHours(1).isAfter(LocalDateTime.now())
                )
                .flatMap(order -> {
                    Order cancelledOrder = new Order(
                            order.id(),
                            order.customerId(),
                            order.items(),
                            Order.OrderStatus.CANCELLED,
                            order.createdAt()
                    );
                    return orderRepository.save(cancelledOrder)
                            .flatMap(saved -> restoreInventory(saved).thenReturn(saved));
                });
    }

    private Mono<Void> updateInventory(Order order) {
        return Flux.fromIterable(order.items())
                .flatMap(item -> productRepository.findById(item.productId())
                        .flatMap(product ->
                                productRepository.updateStock(
                                        product.id(),
                                        product.stockQuantity() - item.quantity()
                                )
                        )
                )
                .then();
    }

    private Mono<Void> restoreInventory(Order order) {
        return Flux.fromIterable(order.items())
                .flatMap(item -> productRepository.findById(item.productId())
                        .flatMap(product ->
                                productRepository.updateStock(
                                        product.id(),
                                        product.stockQuantity() + item.quantity()
                                )
                        )
                )
                .then();
    }

    @Override
    public Mono<Order> getOrder(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Flux<Order> getCustomerOrders(UUID customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}
