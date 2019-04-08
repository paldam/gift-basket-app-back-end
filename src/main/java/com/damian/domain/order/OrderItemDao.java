package com.damian.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Damian on 31.08.2018.
 */
@Repository
public interface OrderItemDao extends JpaRepository<OrderItem,Long> {
}
