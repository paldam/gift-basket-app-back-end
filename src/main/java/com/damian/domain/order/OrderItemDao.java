package com.damian.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderItemDao extends JpaRepository<OrderItem, Long>{

public OrderItem findByOrderItemId(Integer id);
}


