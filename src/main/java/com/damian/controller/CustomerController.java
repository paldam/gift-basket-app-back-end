package com.damian.controller;

import com.damian.model.Basket;
import com.damian.model.Customer;
import com.damian.repository.CustomerDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    private CustomerDao customerDao;

    public CustomerController(CustomerDao customerDao){
        this.customerDao=customerDao;
    }

    @CrossOrigin
    @GetMapping("/customers")
    ResponseEntity<List<Customer>> getCustomers(){
        List<Customer> customerList = customerDao.findAllBy();
        return new ResponseEntity<List<Customer>>(customerList, HttpStatus.OK);
    }

}
