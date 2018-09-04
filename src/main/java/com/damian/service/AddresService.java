package com.damian.service;

import com.damian.model.Address;
import com.damian.model.Order;
import com.damian.repository.AddressDao;
import com.damian.repository.OrderDao;
import com.damian.rest.OrderController;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Created by Damian on 23.08.2018.
 */
@Service
public class AddresService {

    private static final Logger logger = Logger.getLogger(AddresService.class);
    private AddressDao addressDao;
    private OrderDao orderDao;

    public AddresService(AddressDao addressDao, OrderDao orderDao) {
        this.addressDao = addressDao;
        this.orderDao = orderDao;
    }





    public ResponseEntity deleteAddr(Long id, Integer customerId){

        List<Address> customerAddresList = addressDao.findAllAddrByCustomerId(customerId);
        Address customerAddresById = addressDao.findOne(id) ;

        logger.info(customerId);
        logger.info(customerAddresList.size());

        if (customerAddresList.size() == 1) {
            return new ResponseEntity("Nie można usunąć adresu, Podany adres jest jedynym adresem" , HttpStatus.FORBIDDEN);
        }

        if(customerAddresById.getIsPrimaryAddress()==1){
            return new ResponseEntity("Nie można usunąć adresu, Podany adres jest adresem głównym" , HttpStatus.FORBIDDEN);
        }
        if (existsAtLeastOneOrder(id)) {
            return new ResponseEntity("Nie można usunąć adresu, Podany adres jest powiązany z conajmniej jednym zamówieniem" , HttpStatus.FORBIDDEN);
        } else {
            addressDao.deleteByAddressId(id);
            return new ResponseEntity(HttpStatus.OK);        }

    }

    @Transactional
    public void changePrimaryAddr(Long id, Integer customerId){
         this.addressDao.setAllCustomerAdrressPrimaryNo(customerId);
         this.addressDao.setAddreesAsPrimary(id);
    }


    private boolean existsAtLeastOneOrder(Long id){

        List<Order> ordersTmp = orderDao.findByAddress_AddressId(id);

        if (ordersTmp.isEmpty() ) {
            return false;
        }else{
            return true;
        }

    }


}