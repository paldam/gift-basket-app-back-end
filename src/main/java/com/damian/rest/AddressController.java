package com.damian.rest;


import com.damian.model.Address;
import com.damian.model.ZipCode;
import com.damian.model.ZipCodeCompositeKey;
import com.damian.repository.AddressDao;
import com.damian.repository.ZipCodeDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddressController {

    private ZipCodeDao zipCodeDao;
    private AddressDao addressDao;
    public AddressController(AddressDao addressDao, ZipCodeDao zipCodeDao) {
        this.addressDao= addressDao;
        this.zipCodeDao=zipCodeDao;

    }

    @CrossOrigin
    @GetMapping("/customerprimaryaddr/{id}")
    ResponseEntity<Address> getCustomerPrimaryAddr(@PathVariable Integer id){
        Address customerPrimaryAddr = addressDao.findCustomerPrimaryAddrById(id);
        return new ResponseEntity<Address>(customerPrimaryAddr, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/zipcode/{code}")
    ResponseEntity<List<ZipCode>> getCustomerPrimaryAddr(@PathVariable String code){
        List<ZipCode> cityListBycode = zipCodeDao.findByZipCodeCode(code) ;
        return new ResponseEntity<List<ZipCode>>(cityListBycode, HttpStatus.OK);
    }

  
}
