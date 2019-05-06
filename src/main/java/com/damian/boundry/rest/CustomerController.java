package com.damian.boundry.rest;

import com.damian.domain.customer.*;
import com.damian.domain.order.Order_;
import com.damian.dto.CustomerAddressDTO;
import com.damian.domain.order.OrderDao;
import com.damian.domain.order.OrderService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {
    private static final Logger logger = Logger.getLogger(OrderController.class);
    private CustomerDao customerDao;
    private Customer_2Dao customer_2Dao;
    private AddressDao addressDao;
    private OrderDao orderDao;
    private OrderService orderService;
    private CustomerService customerService;

    public CustomerController(CustomerDao customerDao , Customer_2Dao customer_2Dao, AddressDao addressDao, OrderDao orderDao, OrderService orderService, CustomerService customerService){
        this.customerDao=customerDao;
        this.customer_2Dao = customer_2Dao;
        this.addressDao=addressDao;
        this.orderDao = orderDao;
        this.orderService= orderService;
        this.customerService= customerService;
    }

    @Transactional
    @CrossOrigin
    @GetMapping("/convert")
    ResponseEntity<List<Customer>> convert(){



        List<Customer> customerList = customerDao.findAllBy();


        customerList.forEach(customer -> {

            Company company = new Company(customer.getOrganizationName());

            if( customer.getOrganizationName() == null){
                company = null;
            }



            List<Address2> addressList = new ArrayList<>();

            customer.getAddresses().forEach(address -> {

                addressList.add(new Address2(address.getAddress(),address.getZipCode(),address.getCityName(),customer.getName()));
            });

            Customer_2 custTmp = new Customer_2(customer.getName(),customer.getEmail(),customer.getPhoneNumber(),customer.getAdditionalInformation(),company,addressList);


            customer_2Dao.save(custTmp);

        });






        return new ResponseEntity<List<Customer>>(customerList, HttpStatus.OK);
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
