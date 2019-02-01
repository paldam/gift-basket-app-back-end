package com.damian.boundry.rest;

import com.damian.dto.BasketExtStockDao;
import com.damian.model.Basket;
import com.damian.model.BasketExt;
import com.damian.model.BasketType;
import com.damian.repository.BasketDao;
import com.damian.repository.BasketTypeDao;
import com.damian.service.BasketExtService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BasketController {

    private static final Logger logger = Logger.getLogger(BasketController.class);


    private BasketExtService basketExtService;
    private BasketDao basketDao;
   private BasketTypeDao basketTypeDao;

    public BasketController(BasketDao basketDao, BasketTypeDao basketTypeDao, BasketExtService basketExtService) {
        this.basketDao = basketDao;
        this.basketTypeDao = basketTypeDao;
        this.basketExtService = basketExtService;
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
    @CrossOrigin
    @PostMapping("/basketext")
    ResponseEntity<BasketExt> createExternalBasket(@RequestBody BasketExt basketExt)throws URISyntaxException {


        System.out.println("22222" + basketExt.toString());

         basketExtService.saveExternalBasket(basketExt);

        return new ResponseEntity<BasketExt>(basketExt,HttpStatus.CREATED);

    }

    @CrossOrigin
    @PostMapping("/basketextstatus")
    ResponseEntity<BasketExt> externalBasketStatus(@RequestBody BasketExt basketExt)throws URISyntaxException {

        Basket basketToChange = new Basket(basketExt);
        Basket savedBasket= basketDao.save(basketToChange);

        return new ResponseEntity<BasketExt>(basketExt,HttpStatus.CREATED);

    }

    @CrossOrigin
    @GetMapping("/basketsextlist")
    ResponseEntity<List<BasketExt>> getBasketsExtList(){

        List<Basket> basketList = basketDao.findALLExportBasket();

        List<BasketExt> basketExtList = new ArrayList<>();

        basketList.forEach(basket -> {
            BasketExt basketTmp = new BasketExt(basket) ;
            basketTmp.setBasketTotalPrice(basketTmp.getBasketTotalPrice()) ;

            basketExtList.add(basketTmp) ;
        });


        return new ResponseEntity<List<BasketExt>>(basketExtList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/basket_ext_stock")
    ResponseEntity<List<BasketExtStockDao>> getBasketsExtStock(){

        List<Basket> basketList = basketDao.findALLExportBasket();
        List<BasketExtStockDao> basketExtList = new ArrayList<>();

       basketList.forEach(basket -> basketExtList.add(new BasketExtStockDao(basket)) );


        return new ResponseEntity<List<BasketExtStockDao>>(basketExtList, HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/extbaskets")
    ResponseEntity<List<Basket>> getBasketsForExternalPartner(){
        List<Basket> basketList = basketDao.findAllBasketForExternalPartner();
        return new ResponseEntity<List<Basket>>(basketList, HttpStatus.OK);
    }

}
