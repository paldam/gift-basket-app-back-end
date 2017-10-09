package com.damian.controller;

import com.damian.model.Basket;
import com.damian.model.BasketType;
import com.damian.model.Product;
import com.damian.repository.BasketDao;
import com.damian.repository.BasketTypeDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
public class BasketController {

    BasketDao basketDao;
    BasketTypeDao basketTypeDao;

    public BasketController(BasketDao basketDao, BasketTypeDao basketTypeDao) {
        this.basketDao = basketDao;
        this.basketTypeDao = basketTypeDao;
    }



    @CrossOrigin
    @GetMapping("/baskets")
    ResponseEntity<List<Basket>> getBaskets(){
        List<Basket> basketList = basketDao.findAllByOrderByBasketIdDesc();
        return new ResponseEntity<List<Basket>>(basketList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/baskets/types")
    ResponseEntity<List<BasketType>> getBasketsTypes(){
        List<BasketType> basketTypesList = basketTypeDao.findAllBy();
        return new ResponseEntity<List<BasketType>>(basketTypesList, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/baskets")
    ResponseEntity<Basket> createBasket(@RequestBody Basket basket)throws URISyntaxException {
        basketDao.save(basket);

        return new ResponseEntity<Basket>(basket,HttpStatus.CREATED);


    }
}