package com.damian.domain.order;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface OrderStatusDao extends CrudRepository<OrderStatus,Long> {
    public List<OrderStatus> findAllBy();

}
