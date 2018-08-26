package com.damian.rest;


import com.damian.model.Address;
import com.damian.model.ZipCode;
import com.damian.model.ZipCodeCompositeKey;
import com.damian.repository.AddressDao;
import com.damian.repository.ZipCodeDao;
import com.damian.service.AddresService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {

    private ZipCodeDao zipCodeDao;
    private AddressDao addressDao;
    private AddresService addresService;

    public AddressController(AddressDao addressDao, ZipCodeDao zipCodeDao, AddresService addresService) {
        this.addressDao = addressDao;
        this.zipCodeDao = zipCodeDao;
        this.addresService = addresService;

    }

    @CrossOrigin
    @GetMapping("/customerprimaryaddr/{id}")
    ResponseEntity<Address> getCustomerPrimaryAddr(@PathVariable Integer id) {
        Address customerPrimaryAddr = addressDao.findCustomerPrimaryAddrById(id);
        return new ResponseEntity<Address>(customerPrimaryAddr, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/zipcode/{code}")
    ResponseEntity<List<ZipCode>> getCustomerPrimaryAddr(@PathVariable String code) {
        List<ZipCode> cityListBycode = zipCodeDao.findByZipCodeCode(code);
        return new ResponseEntity<List<ZipCode>>(cityListBycode, HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping(value = "/address/",produces = "application/json; charset=utf-8")
    ResponseEntity deleteAddress(@RequestParam Long id, Integer customerId) {

        return addresService.deleteAddr(id,customerId);

    }

    @CrossOrigin
    @DeleteMapping ("/primaryaddress/")
    ResponseEntity changePrimaryAddr(@RequestParam Long id, Integer customerId) {
        

        addresService.changePrimaryAddr(id,customerId);

        return new ResponseEntity(HttpStatus.OK)  ;

    }
}
