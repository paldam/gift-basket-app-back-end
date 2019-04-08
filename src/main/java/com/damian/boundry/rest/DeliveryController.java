package com.damian.boundry.rest;

import com.damian.domain.order.DeliveryType;
import com.damian.domain.order.DeliveryTypeDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class DeliveryController {


    private DeliveryTypeDao deliveryTypeDao;

    DeliveryController(DeliveryTypeDao deliveryTypeDao){

            this.deliveryTypeDao = deliveryTypeDao;
    }

    @CrossOrigin
    @GetMapping("/delivery/types")
    ResponseEntity<List<DeliveryType>> getDeliveryTypes(){
        List<DeliveryType> deliveryTypesList = deliveryTypeDao.findAllBy();
        return new ResponseEntity<List<DeliveryType>>(deliveryTypesList, HttpStatus.OK);
    }
}
