package com.damian.rest;

import com.damian.model.Basket;
import com.damian.model.BasketType;
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
        List<Basket> basketList = basketDao.findAllWithoutDeleted();
        return new ResponseEntity<List<Basket>>(basketList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/deletedbaskets/")
    ResponseEntity<List<Basket>> getDeletedBaskets(){
        List<Basket> basketList = basketDao.findAllDeleted();
        return new ResponseEntity<List<Basket>>(basketList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/basket/{id}")
    ResponseEntity<Basket> getBaskets(@PathVariable Long id){
        Basket basket = basketDao.findOne(id);
        return new ResponseEntity<Basket>(basket, HttpStatus.OK);
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
