package com.damian.boundry.rest;

import com.damian.domain.customer.*;
import com.damian.dto.CustomerAddressDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
public class CustomerController {

    private CustomerDao customerDao;
    private CompanyDao companyDao;
    private CustomerService customerService;
    private CompanyService companyService;

    public CustomerController(CompanyService companyService, CustomerDao customerDao, CompanyDao companyDao, CustomerService customerService) {
        this.customerDao = customerDao;
        this.companyDao = companyDao;
        this.customerService = customerService;
        this.companyService = companyService;
    }

    @GetMapping("/customers")
    ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customerList = customerDao.findAllFetchCompany();
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    ResponseEntity<Customer> getCustomer(@PathVariable Integer id) {
        return customerDao.findByCustomerId(id)
            .map(customer -> ResponseEntity.ok().body(customer))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/customers")
    ResponseEntity<Customer> saveCustomers(@RequestBody Customer customer) {
        Customer savedCustomer = customerDao.save(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/customersaddr")
    ResponseEntity<List<CustomerAddressDTO>> getCustomersWithAddr() {
        List<CustomerAddressDTO> customerList = customerService.getCustomerWithAddressList();
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }

    @DeleteMapping("/customer/{id}")
    ResponseEntity deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomer(id);
    }

    @PostMapping("/company")
    ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company savedCompany = companyDao.save(company);
        return new ResponseEntity<>(savedCompany, HttpStatus.CREATED);
    }

    @PostMapping("/company/merge")
    ResponseEntity<Company> mergeCompanies(@RequestPart(value = "companies") List<Company> companies,
                                           @RequestPart(value = "newcompanyname") String newCompanyName) {
        Company margedCompany = companyService.mergeCompany(companies, newCompanyName);
        return new ResponseEntity<>(margedCompany, HttpStatus.OK);
    }

    @GetMapping("/company")
    ResponseEntity<List<Company>> getCompany() {
        List<Company> companyList = companyDao.findAll();
        return new ResponseEntity<>(companyList, HttpStatus.OK);
    }
}
