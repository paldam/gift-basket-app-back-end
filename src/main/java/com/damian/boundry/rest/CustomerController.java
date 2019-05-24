package com.damian.boundry.rest;

import com.damian.domain.customer.*;
import com.damian.domain.customer.OLD.AddressOLD;
import com.damian.domain.customer.OLD.AddressOLDDao;
import com.damian.domain.customer.OLD.CustomerOLD;
import com.damian.domain.customer.OLD.CustomerOLDDao;
import com.damian.domain.product.Supplier;
import com.damian.dto.CustomerAddressDTO;
import com.damian.domain.order.OrderDao;
import com.damian.domain.order.OrderService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {
    private static final Logger logger = Logger.getLogger(OrderController.class);
    private CustomerDao customerDao;
    private CustomerOLDDao customerOLDDao;
    private CompanyDao companyDao;
    private AddressDao addressDao;
    private AddressOLDDao addressOLDDao;
    private OrderDao orderDao;
    private OrderService orderService;
    private CustomerService customerService;

    public CustomerController(CustomerDao customerDao ,AddressOLDDao addressOLDDao, CustomerOLDDao customerOLDDao, CompanyDao companyDao, AddressDao addressDao, OrderDao orderDao, OrderService orderService, CustomerService customerService){
        this.customerDao=customerDao;
        this.customerOLDDao = customerOLDDao;
        this.companyDao = companyDao;
        this.addressOLDDao = addressOLDDao;
        this.addressDao=addressDao;
        this.orderDao = orderDao;
        this.orderService= orderService;
        this.customerService= customerService;
    }


    @Transactional
    @CrossOrigin
    @GetMapping("/convert")
    ResponseEntity<List<Customer>> convert(){



        List<CustomerOLD> customerList = customerOLDDao.findAll();


        customerList.forEach(customer -> {

            Company company = new Company(customer.getOrganizationName());

            if( customer.getOrganizationName() == null){
                company = null;
            }


            Customer custTmp = new Customer(customer.getCustomerId(),customer.getName(),customer.getEmail(),customer.getPhoneNumber(),customer.getAdditionalInformation(),company);

            customerDao.save(custTmp);

        });


        return null;
    }

    @Transactional
    @CrossOrigin
    @GetMapping("/convert2")
    ResponseEntity<List<Address>> convert2(){



        List<AddressOLD> adresList = addressOLDDao.findAll();


        adresList.forEach(adres -> {



                Address adrestTmp = new Address(adres.getAddressId(),adres.getAddress(),adres.getZipCode(),adres.getCityName(),adres.getCustomer().getName(),adres.getCustomer().getPhoneNumber(),null);
                addressDao.save(adrestTmp);
            });





        return null;
    }



    @CrossOrigin
    @PostMapping("/company")
    ResponseEntity<Company> createCompany(@RequestBody Company company ) {
        Company savedCompany = companyDao.save(company);


        return new ResponseEntity<Company>(savedCompany,HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/company")
    ResponseEntity<List<Company>> getCompany(){
        List<Company> companyList = companyDao.findAll();
        return new ResponseEntity<List<Company>>(companyList, HttpStatus.OK);
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
