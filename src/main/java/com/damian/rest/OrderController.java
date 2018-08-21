package com.damian.rest;

import com.damian.model.*;
import com.damian.repository.*;
import com.damian.service.DbFileService;
import com.damian.service.OrderService;
import com.damian.util.PdfDeliveryConfirmation;
import com.damian.util.PdfGenerator;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class);

    private OrderDao orderDao;
    private OrderService orderService;
    private OrderStatusDao orderStatusDao;
    private ProductDao productDao;
    private DbFileDao dbFileDao;
    private DbFileService dbFileService;
    OrderController(DbFileService dbFileService, OrderDao orderDao, OrderService orderService, DeliveryTypeDao deliveryTypeDao, OrderStatusDao orderStatusDao, ProductDao productDao,
                    DbFileDao dbFileDao){
        this.orderDao=orderDao;
        this.orderService=orderService;
        this.orderStatusDao=orderStatusDao;
        this.productDao=productDao;
        this.dbFileDao= dbFileDao;
        this.dbFileService = dbFileService;
        
    }
    @CrossOrigin
    @GetMapping(value = "/order/{id}")
    ResponseEntity<Order> getOrder(@PathVariable Long id){
        Order order = orderDao.findOne(id);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/orders")
    ResponseEntity<List<Order>> getOrders(){
        List<Order> ordersList = orderDao.findAllWithoutDeleted();
        return new ResponseEntity<List<Order>>(ordersList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/order/customer/{id}")
    ResponseEntity<List<Order>> getOrdersByCustomer( @PathVariable Integer id) {

        List<Order> ordersList = orderDao.findByCustomerId(id);
        return new ResponseEntity<List<Order>>(ordersList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/orders/daterange")
    ResponseEntity<List<Order>> getOrdersByDateRange(
            @RequestParam(value="startDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value="endDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){
        List<Order> ordersList = orderDao.findOrdersByDateRange(startDate,endDate);
        return new ResponseEntity<List<Order>>(ordersList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/order_status")
    ResponseEntity<List<OrderStatus>> getOrderStatus(){
        List<OrderStatus> ordersStatusList = orderStatusDao.findAllBy();
        return new ResponseEntity<List<OrderStatus>>(ordersStatusList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/orders/products_to_order/daterange")
    ResponseEntity<List<Order>> getProductsToOrder(
            @RequestParam(value="startDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value="endDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){
        List<Order> productToOrderList = orderDao.findProductToOrder(startDate,endDate);
        return new ResponseEntity<List<Order>>(productToOrderList, HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping("/orders")
    ResponseEntity<Order> createOrder(@RequestBody Order order )throws URISyntaxException {

        orderService.createOrder(order);

        return new ResponseEntity<Order>(order,HttpStatus.CREATED);
    }


    @Bean(name = "multipartResolver")     
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(112000000); //12MB
        return multipartResolver;
    }

}
