package com.damian.boundry.rest;

import com.damian.domain.order.Order;
import com.damian.domain.prize.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prize")
public class PrizeController {

    private PrizeDao prizeDao;
    private PrizeOrderDao prizeOrderDao;
    private PrizeOrderService prizeOrderService;

    public PrizeController(PrizeDao prizeDao, PrizeOrderDao prizeOrderDao, PrizeOrderService prizeOrderService) {
        this.prizeDao = prizeDao;
        this.prizeOrderDao = prizeOrderDao;
        this.prizeOrderService = prizeOrderService;
    }

    @CrossOrigin
    @PostMapping("/order")
    ResponseEntity<PrizeOrder> createOrder(@RequestBody PrizeOrder prizeOrder) {
        PrizeOrder savedOrder = prizeOrderService.saveOrder(prizeOrder);
        return new ResponseEntity<PrizeOrder>(savedOrder, HttpStatus.CREATED);
    }


    @CrossOrigin
    @GetMapping("orders")
    ResponseEntity<List<PrizeOrder>> getPrizeOrders() {
        List<PrizeOrder> prizeOrderList = prizeOrderDao.findAllByOrderByOrderDateDesc();
        return new ResponseEntity<List<PrizeOrder>>(prizeOrderList, HttpStatus.OK);
    }

    @GetMapping(value = "/order/{id}")
    ResponseEntity<PrizeOrder> getPrizeOrder(@PathVariable Long id) {
        Optional<PrizeOrder> order = prizeOrderDao.findById(id);
        return new ResponseEntity<PrizeOrder>(order.get(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/prizelist")
    ResponseEntity<List<Prize>> getPrizes() {
        List<Prize> prizeList = prizeDao.findAllBy();


        return new ResponseEntity<List<Prize>>(prizeList, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/order/status/{id}/{statusId}", produces = "text/plain;charset=UTF-8")
    ResponseEntity changeOrderStatus(@PathVariable Long id, @PathVariable Integer statusId) {

        PrizeOrder updatingOrder = prizeOrderDao.findById(id).get();

        PrizeOrderStatus updattingOrderNewStatus = new PrizeOrderStatus();
        updattingOrderNewStatus.setOrderStatusId(statusId);
        updatingOrder.setPrizeOrderStatus(updattingOrderNewStatus);

            prizeOrderService.updateOrder(updatingOrder);

            return new ResponseEntity<>(null, HttpStatus.OK);

    }


}
