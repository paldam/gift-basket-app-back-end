package com.damian.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemDao extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrderItemId(Integer id);
}


