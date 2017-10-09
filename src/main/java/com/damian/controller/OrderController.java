package com.damian.controller;

import com.damian.model.*;
import com.damian.repository.CustomerDao;
import com.damian.repository.DeliveryTypeDao;
import com.damian.repository.OrderDao;
import com.damian.service.OrderService;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
public class OrderController {

    private OrderDao orderDao;
    private OrderService orderService;
    private DeliveryTypeDao deliveryTypeDao;

    OrderController(OrderDao orderDao, OrderService orderService, DeliveryTypeDao deliveryTypeDao){
        this.orderDao=orderDao;
        this.orderService=orderService;
        this.deliveryTypeDao = deliveryTypeDao;
    }

    @CrossOrigin
    @GetMapping("/orders")
    ResponseEntity<List<Order>> getOrders(){
        List<Order> ordersList = orderDao.findAllBy();
        return new ResponseEntity<List<Order>>(ordersList, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/orders")
    ResponseEntity<Order> createOrder(@RequestBody Order order )throws URISyntaxException {

        orderService.createOrder(order);

        return new ResponseEntity<Order>(order,HttpStatus.CREATED);
    }
    
}
