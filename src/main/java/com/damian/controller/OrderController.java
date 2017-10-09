package com.damian.controller;

import com.damian.model.*;
import com.damian.repository.DeliveryTypeDao;
import com.damian.repository.OrderDao;
import com.damian.service.OrderService;
import com.damian.util.PdfGenerator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class OrderController {

    private OrderDao orderDao;
    private OrderService orderService;

    OrderController(OrderDao orderDao, OrderService orderService, DeliveryTypeDao deliveryTypeDao){
        this.orderDao=orderDao;
        this.orderService=orderService;
    }

    @CrossOrigin
    @GetMapping("/orders")
    ResponseEntity<List<Order>> getOrders(){
        List<Order> ordersList = orderDao.findAllByOrderByOrderIdDesc();
        return new ResponseEntity<List<Order>>(ordersList, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/orders")
    ResponseEntity<Order> createOrder(@RequestBody Order order )throws URISyntaxException {

        orderService.createOrder(order);

        return new ResponseEntity<Order>(order,HttpStatus.CREATED);
    }

    @RequestMapping(value = "/order/pdf", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPdf() throws IOException {



        ByteArrayInputStream bis = PdfGenerator.orderPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
