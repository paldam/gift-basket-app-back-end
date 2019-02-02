package com.damian.boundry.rest;

import com.damian.dto.CustomerAddressDTO;
import com.damian.model.Customer;
import com.damian.repository.AddressDao;
import com.damian.repository.CustomerDao;
import com.damian.domain.order.OrderDao;
import com.damian.service.CustomerService;
import com.damian.domain.order.OrderService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {
    private static final Logger logger = Logger.getLogger(OrderController.class);
    private CustomerDao customerDao;
    private AddressDao addressDao;
    private OrderDao orderDao;
    private OrderService orderService;
    private CustomerService customerService;

    public CustomerController(CustomerDao customerDao , AddressDao addressDao, OrderDao orderDao, OrderService orderService, CustomerService customerService){
        this.customerDao=customerDao;
        this.addressDao=addressDao;
        this.orderDao = orderDao;
        this.orderService= orderService;
        this.customerService= customerService;
    }

    @CrossOrigin
    @GetMapping("/customers")
    ResponseEntity<List<Customer>> getCustomers(){
        List<Customer> customerList = customerDao.findAllBy();
        return new ResponseEntity<List<Customer>>(customerList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/customer/{id}")
    ResponseEntity<Customer> getCustomer(@PathVariable Integer id){
        Customer customer = customerDao.findByCustomerId(id) ;
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
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


    @CrossOrigin
    @GetMapping("/customersaddr")
    ResponseEntity<List<CustomerAddressDTO>> getCustomersWithAddr(){
        List<CustomerAddressDTO> customerList = customerService.getCustomerWithAddressList();
        return new ResponseEntity<List<CustomerAddressDTO>>(customerList, HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping ("/customer/{id}")
    ResponseEntity deleteCustomer(@PathVariable Integer id) {

        return customerService.deleteCustomer(id) ;

    }


}
