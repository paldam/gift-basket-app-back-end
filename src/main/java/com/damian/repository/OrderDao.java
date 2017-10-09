package com.damian.repository;

import com.damian.model.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface OrderDao extends CrudRepository<Order,Long> {
    public List<Order> findAllBy();
    public List<Order> findAllByOrderByOrderIdDesc();
}
