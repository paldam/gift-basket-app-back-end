package com.damian.repository;

import com.damian.model.Order;
import com.damian.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Damian on 31.08.2018.
 */
@Repository
public interface OrderItemDao extends JpaRepository<OrderItem,Long> {
}
