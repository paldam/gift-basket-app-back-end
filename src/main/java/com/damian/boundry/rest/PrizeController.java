package com.damian.boundry.rest;

import com.damian.domain.prize.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/prizelist")
    ResponseEntity<List<Prize>> getPrizes() {
        List<Prize> prizeList = prizeDao.findAllBy();


        return new ResponseEntity<List<Prize>>(prizeList, HttpStatus.OK);
    }
}
