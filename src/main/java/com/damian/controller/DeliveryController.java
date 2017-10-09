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
public class DeliveryController {


    private DeliveryTypeDao deliveryTypeDao;

    DeliveryController(DeliveryTypeDao deliveryTypeDao){

            this.deliveryTypeDao = deliveryTypeDao;
    }

    @CrossOrigin
    @GetMapping("/delivery")
    ResponseEntity<List<DeliveryType>> getDeliveryTypes(){
        List<DeliveryType> deliveryTypesList = deliveryTypeDao.findAllBy();
        return new ResponseEntity<List<DeliveryType>>(deliveryTypesList, HttpStatus.OK);
    }
}
