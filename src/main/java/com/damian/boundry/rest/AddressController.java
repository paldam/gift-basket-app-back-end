package com.damian.boundry.rest;

import com.damian.domain.customer.Address;
import com.damian.domain.customer.ZipCode;
import com.damian.domain.customer.AddressDao;
import com.damian.domain.customer.ZipCodeDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
public class AddressController {

    private final ZipCodeDao zipCodeDao;
    private final AddressDao addressDao;

    public AddressController(AddressDao addressDao, ZipCodeDao zipCodeDao) {
        this.addressDao = addressDao;
        this.zipCodeDao = zipCodeDao;
    }

    @GetMapping("/customerprimaryaddr/{id}")
    ResponseEntity getCustomerPrimaryAddr(@PathVariable Integer id) {
        return addressDao.findCustomerPrimaryAddrById(id)
            .map(address -> ResponseEntity.ok().body(address))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/customerlastaddr/{id}")
    ResponseEntity getCustomerLastAddr(@PathVariable Integer id) {
        return addressDao.findCustomerLastAddr(id)
            .map(address -> ResponseEntity.ok().body(address))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/zipcode/{code}")
    ResponseEntity<List<ZipCode>> getCustomerPrimaryAddr(@PathVariable String code) {
        List<ZipCode> cityListBycode = zipCodeDao.findByZipCodeCode(code);
        return new ResponseEntity<>(cityListBycode, HttpStatus.OK);
    }

    @GetMapping("/address/{companyId}")
    ResponseEntity<Set<Address>> getAddressByComapny(@PathVariable Long companyId) {
        Set<Address> addressList = addressDao.findAddressByCompanyId(companyId);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }
}
