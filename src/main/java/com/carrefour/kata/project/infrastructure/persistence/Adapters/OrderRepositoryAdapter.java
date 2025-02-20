package com.carrefour.kata.project.infrastructure.persistence.Adapters;

import com.carrefour.kata.project.domain.CartItem;
import com.carrefour.kata.project.domain.Order;
import com.carrefour.kata.project.infrastructure.entities.OrderEntity;
import com.carrefour.kata.project.infrastructure.entities.OrderItemEntity;
import com.carrefour.kata.project.infrastructure.persistence.OrderItemR2dbcRepository;
import com.carrefour.kata.project.infrastructure.persistence.OrderR2dbcRepository;
import com.carrefour.kata.project.ports.secondary.OrderRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class OrderRepositoryAdapter implements OrderRepository {
    private final OrderR2dbcRepository orderRepository;
    private final OrderItemR2dbcRepository orderItemRepository;

    public OrderRepositoryAdapter(
            OrderR2dbcRepository orderRepository,
            OrderItemR2dbcRepository orderItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Mono<Order> save(Order order) {
        OrderEntity orderEntity = new OrderEntity(
                order.id(),
                order.customerId(),
                order.status().name(),
                order.createdAt()
        );

        return orderRepository.save(orderEntity)
                .flatMap(savedOrder -> Flux.fromIterable(order.items())
                        .flatMap(item -> {
                            OrderItemEntity itemEntity = new OrderItemEntity(
                                    UUID.randomUUID(),
                                    savedOrder.id(),
                                    item.productId(),
                                    item.quantity()
                            );
                            return orderItemRepository.save(itemEntity);
                        })
                        .collectList()
                        .thenReturn(order));
    }

    @Override
    public Mono<Order> findById(UUID id) {
        return orderRepository.findById(id)
                .flatMap(orderEntity ->
                        orderItemRepository.findByOrderId(orderEntity.id())
                                .map(item -> new CartItem(item.productId(), item.quantity()))
                                .collectList()
                                .map(items -> new Order(
                                        orderEntity.id(),
                                        orderEntity.customerId(),
                                        items,
                                        Order.OrderStatus.valueOf(orderEntity.status()),
                                        orderEntity.createdAt()
                                ))
                );
    }

    @Override
    public Flux<Order> findByCustomerId(UUID customerId) {
        return orderRepository.findByCustomerId(customerId)
                .flatMap(orderEntity ->
                        orderItemRepository.findByOrderId(orderEntity.id())
                                .map(item -> new CartItem(item.productId(), item.quantity()))
                                .collectList()
                                .map(items -> new Order(
                                        orderEntity.id(),
                                        orderEntity.customerId(),
                                        items,
                                        Order.OrderStatus.valueOf(orderEntity.status()),
                                        orderEntity.createdAt()
                                ))
                );
    }
}
