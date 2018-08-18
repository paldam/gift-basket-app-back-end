package com.damian.rest;

import com.damian.model.Address;
import com.damian.model.Basket;
import com.damian.model.Customer;
import com.damian.model.Order;
import com.damian.repository.AddressDao;
import com.damian.repository.CustomerDao;
import com.damian.repository.OrderDao;
import com.damian.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {
    private static final Logger logger = Logger.getLogger(OrderController.class);
    private CustomerDao customerDao;
    private AddressDao addressDao;
    private OrderDao orderDao;
    private OrderService orderService;

    public CustomerController(CustomerDao customerDao , AddressDao addressDao, OrderDao orderDao, OrderService orderService){
        this.customerDao=customerDao;
        this.addressDao=addressDao;
        this.orderDao = orderDao;
        this.orderService= orderService;
    }

    @CrossOrigin
    @GetMapping("/customers")
    ResponseEntity<List<Customer>> getCustomers(){
        List<Customer> customerList = customerDao.findAllBy();
        return new ResponseEntity<List<Customer>>(customerList, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/customers")
    ResponseEntity<Customer> saveCustomers(@RequestBody Customer customer){
        Customer savedCustomer = customerDao.save(customer);
        return new ResponseEntity<Customer>(savedCustomer, HttpStatus.CREATED);
    }

    
    @CrossOrigin
    @PostMapping("/customers2")

    void getCustomers2() {
        //orderService.changeDBStructure();

    }

}
