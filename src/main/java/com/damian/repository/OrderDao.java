package com.damian.repository;

import com.damian.model.Order;
import com.damian.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface OrderDao extends CrudRepository<Order,Long> {
    public List<Order> findAllBy();
    public List<Order> findAllByOrderByOrderIdDesc();
    public List<Order> findAllByOrderStatus_OrderStatusId(Integer i);
    public Order findByOrderId(Integer id);


    @Query(value = "SELECT * FROM orders WHERE order_status_id != 99", nativeQuery = true)
    public List<Order> findAllWithoutDeleted();
}
