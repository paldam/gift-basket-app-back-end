package com.damian.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface OrderItemDao extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrderItemId(Integer id);

    @Query(value = "select oi from OrderItem oi LEFT JOIN FETCH oi.basket b LEFT JOIN FETCH b.basketItems bi" +
        " LEFT JOIN FETCH bi.product p where  oi.orderItemId IN ?1")
    List<OrderItem> findByOrderItemId(List<Integer> ids);
}


