package com.damian.service;

import com.damian.dto.CustomerAddressDTO;
import com.damian.model.Address;
import com.damian.model.Customer;
import com.damian.repository.AddressDao;
import com.damian.repository.CustomerDao;
import com.damian.rest.OrderController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private static final Logger logger = Logger.getLogger(CustomerService.class);
    private CustomerDao customerDao;
    private AddressDao addressDao;

    public CustomerService(CustomerDao customerDao, AddressDao addressDao) {
        this.addressDao =addressDao;
        this.customerDao = customerDao;
    }


    public List<CustomerAddressDTO> getCustomerWithAddressList() {

        List<Customer> custList = customerDao.findAllBy();
        List<CustomerAddressDTO> custAddrList = new ArrayList<>();

         custList.forEach(customer -> {

          Address tmpAddr = addressDao.findCustomerPrimaryAddrById(customer.getCustomerId());
          logger.info("dsdsd :" + customer.getCustomerId()+ " " +tmpAddr);

          custAddrList.add(new CustomerAddressDTO(customer,tmpAddr));
         });

         return  custAddrList;
    }

}
