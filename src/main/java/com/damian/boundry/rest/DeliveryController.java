package com.damian.boundry.rest;

import com.damian.domain.order.DeliveryType;
import com.damian.domain.order.DeliveryTypeDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class DeliveryController {

    private DeliveryTypeDao deliveryTypeDao;

    DeliveryController(DeliveryTypeDao deliveryTypeDao) {
        this.deliveryTypeDao = deliveryTypeDao;
    }

    @GetMapping("/delivery/types")
    ResponseEntity<List<DeliveryType>> getDeliveryTypes() {
        List<DeliveryType> deliveryTypesList = deliveryTypeDao.findAllBy();
        return new ResponseEntity<>(deliveryTypesList, HttpStatus.OK);
    }
}
