package com.damian.boundry.rest;

import com.damian.domain.audit.OrderEditAudit;
import com.damian.domain.order.*;
import com.damian.domain.order_file.DbFileDao;
import com.damian.domain.product.ProductDao;
import com.damian.dto.NumberOfBasketOrderedByDate;
import com.damian.dto.OrderDto;
import com.damian.domain.order.exceptions.OrderStatusException;
import com.damian.domain.order_file.DbFileService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.net.URISyntaxException;
import java.util.*;

@RestController
public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class);

    private OrderDao orderDao;
    private OrderService orderService;
    private OrderStatusDao orderStatusDao;
    private ProductDao productDao;
    private DbFileDao dbFileDao;
    private DbFileService dbFileService;
    private OrderEditAuditDao orderEditAuditDao;
    OrderController(DbFileService dbFileService, OrderDao orderDao, OrderService orderService, DeliveryTypeDao deliveryTypeDao, OrderStatusDao orderStatusDao, ProductDao productDao,
                    DbFileDao dbFileDao,OrderEditAuditDao orderEditAuditDao){
        this.orderDao=orderDao;
        this.orderService=orderService;
        this.orderStatusDao=orderStatusDao;
        this.productDao=productDao;
        this.dbFileDao= dbFileDao;
        this.dbFileService = dbFileService;
        this.orderEditAuditDao = orderEditAuditDao;
        
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
    @GetMapping("/orderitem")
    ResponseEntity<List<Order>> getOrderItem(){
        List<Order> ordersList = orderDao.findAllWithoutDeleted();
        return new ResponseEntity<List<Order>>(ordersList, HttpStatus.OK);
    }

//    @CrossOrigin
//    @GetMapping("/orderswithattch/")
//    ResponseEntity<List<Order>> getOrdersWithAttach(){
//        List<Order> ordersList = orderDao.findAllByDbFileIsNotNull();
//        return new ResponseEntity<List<Order>>(ordersList, HttpStatus.OK);
//    }



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
    @GetMapping("/orders/products_to_order_without_deleted_by_delivery_date/daterange")
    ResponseEntity<List<Order>> getProductsToOrderWithoutDeletedByDeliveryDate(
        @RequestParam(value="startDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
        @RequestParam(value="endDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){

        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        c.add(Calendar.DATE, 1);
        Date endDateconvertedToTimeStamp = c.getTime();

        List<Order> productToOrderList = orderDao.findProductToOrderWithoutDeletedOrderByDeliveryDate(startDate,endDateconvertedToTimeStamp);
        return new ResponseEntity<List<Order>>(productToOrderList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/orders/products_to_order_without_deleted_by_order_date/daterange")
    ResponseEntity<List<Order>> getProductsToOrderWithoutDeletedByOrderDate(
        @RequestParam(value="startDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
        @RequestParam(value="endDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){

        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        c.add(Calendar.DATE, 1);
        Date endDateconvertedToTimeStamp = c.getTime();

        List<Order> productToOrderList = orderDao.findProductToOrderWithoutDeletedOrderByOrderDate(startDate,endDateconvertedToTimeStamp);
        return new ResponseEntity<List<Order>>(productToOrderList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/baskets/statistic/daterange")
    ResponseEntity<List<NumberOfBasketOrderedByDate>> getNumberOfBasketOrdered(
            @RequestParam(value="startDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value="endDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {


                        Calendar c = Calendar.getInstance();
                        c.setTime(endDate);
                        c.add(Calendar.DATE, 1);
                        Date endDateconvertedToTimeStamp = c.getTime();


        List<NumberOfBasketOrderedByDate> basketList = orderDao.getNumberOfBasketOrdered(startDate,endDateconvertedToTimeStamp) ;
        return new ResponseEntity<List<NumberOfBasketOrderedByDate>>(basketList, HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/order/statistic/orderdaterange")
    ResponseEntity<List<Order>> getOrdersByBasket(
        @RequestParam(value="basketId", required=true) Long basketId,
        @RequestParam(value="startDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
        @RequestParam(value="endDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){


        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        c.add(Calendar.DATE, 1);
        Date endDateconvertedToTimeStamp = c.getTime();


        List<Order> orderList = orderDao.findAllOrderByBasketIdAndOrderDate(basketId,startDate,endDateconvertedToTimeStamp) ;
        return new ResponseEntity<List<Order>>(orderList, HttpStatus.OK);
    }




    @CrossOrigin
    @GetMapping("/baskets/statistic/orderdaterange")
    ResponseEntity<List<NumberOfBasketOrderedByDate>> getNumberOfBasketOrderedFilteredByOrderDate(
            @RequestParam(value="startDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value="endDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){


        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        c.add(Calendar.DATE, 1);
        Date endDateconvertedToTimeStamp = c.getTime();


        List<NumberOfBasketOrderedByDate> basketList = orderDao.getNumberOfBasketOrderedFilteredByOrderDate(startDate,endDateconvertedToTimeStamp) ;
        return new ResponseEntity<List<NumberOfBasketOrderedByDate>>(basketList, HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/orderdao")
    ResponseEntity<List<OrderDto>> getOrderDao( )
    {
        List<OrderDto> orderDtoList =  orderService.getOrderDao();
        return new ResponseEntity<List<OrderDto>>(orderDtoList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/orderstats")
    ResponseEntity<List<OrderDto>> getOrderStats( )
    {
        List<OrderDto> orderDtoList =  orderService.getOrderStats();
        return new ResponseEntity<List<OrderDto>>(orderDtoList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/order/customer/{id}")
    ResponseEntity<List<OrderDto>> getOrdersByCustomer( @PathVariable Integer id) {

        List<OrderDto> ordersList = orderService.getOrderDaoByCustomer(id);
        return new ResponseEntity<List<OrderDto>>(ordersList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/order/audit/{id}")
    ResponseEntity<List<OrderEditAudit>> getOrderAudit(@PathVariable Long id )
    {
        List<OrderEditAudit> orderAuditList = orderEditAuditDao.findByOrderId(id) ;
        return new ResponseEntity<List<OrderEditAudit>>(orderAuditList, HttpStatus.OK);
    }



//    @CrossOrigin
//    @GetMapping("/aaa")
//    ResponseEntity<List<NumberProductsToChangeStock>> getOrderAudit( )
//    {
//        List<NumberProductsToChangeStock> list = productDao.numberProductsToChangeStock(859L) ;
//        return new ResponseEntity<List<NumberProductsToChangeStock>>(list, HttpStatus.OK);
//    }





    @CrossOrigin
    @PostMapping("/orders")
    ResponseEntity createOrder(@RequestBody Order order )throws URISyntaxException, OrderStatusException {


        try {
            orderService.createOrUpdateOrder(order);
            return new ResponseEntity<Order>(order, HttpStatus.CREATED);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping(value = "/order/status/{id}/{statusId}",produces = "text/plain;charset=UTF-8")
    ResponseEntity changeOrderStatus(@PathVariable Long id, @PathVariable Integer statusId)throws URISyntaxException, OrderStatusException {


        Order updattingOrder =  orderDao.findByOrderId(id);
        OrderStatus updattingOrderNewStatus =new OrderStatus();
        updattingOrderNewStatus.setOrderStatusId(statusId);
        updattingOrder.setOrderStatus(updattingOrderNewStatus);

        try {
            orderService.createOrUpdateOrder(updattingOrder);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }


    }

    @CrossOrigin
    @DeleteMapping ("/order/{id}")
    ResponseEntity deleteOrderPermanent(@PathVariable Long id) {
        
        orderDao.delete(id);


        return new ResponseEntity(HttpStatus.OK)  ;

    }
    


    @Bean(name = "multipartResolver")     
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(112000000); //12MB
        return multipartResolver;
    }

}
