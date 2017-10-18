package com.damian.repository;

import com.damian.model.Order;
import com.damian.model.OrderStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface OrderStatusDao extends CrudRepository<OrderStatus,Long> {
    public List<OrderStatus> findAllBy();

}
